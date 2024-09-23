/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
 */

package me.amlu.service;

import lombok.NonNull;
import me.amlu.config.JwtProvider;
import me.amlu.model.User;
import me.amlu.repository.UserRepository;
import me.amlu.service.exceptions.CustomerNotFoundException;
import me.amlu.service.exceptions.UserNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static me.amlu.common.SecurityUtil.getAuthenticatedUser;

@Service
public class UserServiceImp implements UserService {

    private static final Logger log = LogManager.getLogger(UserServiceImp.class);
    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;


    public UserServiceImp(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public void deleteUser(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        user.setDeletedAt(Instant.now());
        user.setDeletedBy(getAuthenticatedUser());
        userRepository.save(user);

    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    @Async
    @Transactional
    public void permanentlyDeleteUser(Long userId) throws UserNotFoundException {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found."));
            userRepository.deleteById(userId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UserNotFoundException("User not found.");
        }
    }

    @Override
    public User updateUserName(Long userId, String fullName) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        if (user == null || user.getDeletedAt() != null) {
            throw new UserNotFoundException("User not found.");
        }
        user.setFullName(fullName);
        user.setUpdatedAt(Instant.now());
        user.setUpdatedBy(getAuthenticatedUser());
        userRepository.save(user);
        return user;
    }

    @Override
    public User updateEmail(Long userId, String email) throws UserNotFoundException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        if (user == null || user.getDeletedAt() != null) {
            throw new UserNotFoundException("User not found.");
        }
        user.setEmail(email);
        user.setUpdatedAt(Instant.now());
        user.setUpdatedBy(getAuthenticatedUser());
        userRepository.save(user);
        return user;
    }

    @Override
    public User updatePassword(Long userId, String password) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        if (user == null || user.getDeletedAt() != null) {
            throw new UserNotFoundException("User not found.");
        }
        user.setPassword(password);
        user.setUpdatedAt(Instant.now());
        user.setUpdatedBy(getAuthenticatedUser());
        userRepository.save(user);
        return user;
    }

    @Override
    public List<User> findUsersDeletedBefore(Instant anonymizationThreshold) throws UserNotFoundException {

        // Find all users deleted before the threshold
        return userRepository.findByDeletedAtBefore(anonymizationThreshold);

    }

    @Override
    public Optional<Object> findUserById(@NonNull Long customerId) throws CustomerNotFoundException {

        return Optional.of(userRepository.findById(customerId));
    }

    @Override
    public User findUserByJwtToken(String token) throws UserNotFoundException {
        String email = jwtProvider.getEmailFromJwtToken(token);

        User user = findUserByEmail(email);
        if (email == null || email.isEmpty() || user.getDeletedAt() != null) {
            throw new UserNotFoundException("User not found.");
        }
        return user;
    }

    @Override
    public User findUserByEmail(String email) throws UserNotFoundException {

        User user = userRepository.findByEmail(email);

        if (user == null || user.getDeletedAt() != null) {
            throw new UserNotFoundException("User not found with email: " + email);
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);
        if (user == null || user.getDeletedAt() != null) {
            throw new UsernameNotFoundException("User not found with email: " + username + ".");
        }
        return (UserDetails) user;
    }
}
