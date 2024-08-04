package me.amlu.response;

import lombok.Data;
import me.amlu.model.USER_ROLE;

@Data
public class AuthResponse {
    private String jwt;
    private String message;
    private USER_ROLE role;

    public AuthResponse(String emailAlreadyExist, USER_ROLE userRole) {
        this.message = emailAlreadyExist;
        this.role = userRole;
    }
}
