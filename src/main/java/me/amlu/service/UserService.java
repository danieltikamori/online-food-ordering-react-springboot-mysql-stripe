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
import me.amlu.model.User;
import me.amlu.service.exceptions.CustomerNotFoundException;
import me.amlu.service.exceptions.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public interface UserService extends UserDetailsService {

    public User findUserByJwtToken(String token) throws UserNotFoundException;

    public User findUserByEmail(String email) throws UserNotFoundException;

    // Logical user deletion
    void deleteUser(Long userId) throws UserNotFoundException;

    // Permanent user deletion
    void permanentlyDeleteUser(Long userId) throws UserNotFoundException;


    User updateUserName(Long userId, String fullName) throws UserNotFoundException;

    User updateEmail(Long userId, String email) throws UserNotFoundException;

    User updatePassword(Long userId, String password) throws UserNotFoundException;

    List<User> findUsersDeletedBefore(Instant anonymizationThreshold) throws UserNotFoundException;

    Optional<Object> findUserById(@NonNull Long customerId) throws CustomerNotFoundException;
}
