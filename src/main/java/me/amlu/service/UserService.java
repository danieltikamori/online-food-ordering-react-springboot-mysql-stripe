package me.amlu.service;

import me.amlu.model.User;
import me.amlu.service.Exceptions.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public interface UserService extends UserDetailsService {

    public User findUserByJwtToken(String token) throws UserNotFoundException;

    public User findUserByEmail(String email) throws UserNotFoundException;

    void deleteUser(Long userId) throws UserNotFoundException;

    User updateUserName(Long userId, String fullName) throws UserNotFoundException;

    User updateEmail(Long userId, String email) throws UserNotFoundException;

    User updatePassword(Long userId, String password) throws UserNotFoundException;

    List<User> findUsersDeletedBefore(Instant anonymizationThreshold) throws UserNotFoundException;
}
