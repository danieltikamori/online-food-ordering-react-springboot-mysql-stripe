package me.amlu.service;

import me.amlu.config.JwtProvider;
import me.amlu.model.User;
import me.amlu.repository.UserRepository;
import me.amlu.service.Exceptions.UserNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;



    public UserServiceImp(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    public void deleteUser(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        user.setDeletedAt(Instant.now());
        user.setDeletedBy((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        userRepository.save(user);

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
        user.setUpdatedBy((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
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
        user.setUpdatedBy((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
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
        user.setUpdatedBy((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        userRepository.save(user);
        return user;
    }

    @Override
    public List<User> findUsersDeletedBefore(Instant anonymizationThreshold) throws UserNotFoundException {

        // Find all users deleted before the threshold
        return userRepository.findByDeletedAtBefore(anonymizationThreshold);

    }

    @Override
    public User findUserByJwtToken(String token) throws UserNotFoundException {
        String email =jwtProvider.getEmailFromJwtToken(token);

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
