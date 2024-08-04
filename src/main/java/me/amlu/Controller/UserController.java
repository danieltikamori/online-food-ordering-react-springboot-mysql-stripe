package me.amlu.Controller;

import me.amlu.model.User;
import me.amlu.service.UserNotFoundException;
import me.amlu.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<User> findUserByJwtToken(@RequestHeader("Authorization") String token) throws UserNotFoundException {
        User user = userService.findUserByJwtToken(token);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
