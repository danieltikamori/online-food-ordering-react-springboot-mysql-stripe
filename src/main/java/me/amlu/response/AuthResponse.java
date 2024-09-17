/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.response;

import lombok.Data;
import me.amlu.model.UserRole;

@Data
public class AuthResponse {
    private String jwt;
    private String message;
    private UserRole role;

    public AuthResponse(String emailAlreadyExist, UserRole userRole) {
        this.message = emailAlreadyExist;
        this.role = userRole;
    }
}
