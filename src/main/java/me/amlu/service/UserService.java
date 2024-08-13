package me.amlu.service;

import me.amlu.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends UserDetailsService {

    public User findUserByJwtToken(String token) throws UserNotFoundException;

    public User findUserByEmail(String email) throws UserNotFoundException;

    void deleteUser(Long userId) throws UserNotFoundException;
}
