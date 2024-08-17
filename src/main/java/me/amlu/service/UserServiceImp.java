package me.amlu.service;

import me.amlu.config.JwtProvider;
import me.amlu.model.User;
import me.amlu.repository.UserRepository;
import me.amlu.service.Exceptions.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;

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
        userRepository.save(user);
    }

    @Override
    public User findUserByJwtToken(String token) throws UserNotFoundException {
        String email =jwtProvider.getEmailFromJwtToken(token);

        User user = findUserByEmail(email);
        return user;
    }

    @Override
    public User findUserByEmail(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserNotFoundException("User not found with email: " + email);
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
