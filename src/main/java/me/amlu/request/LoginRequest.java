/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import me.amlu.config.StrongPassword;

@Data
public class LoginRequest {

    @Size(max = 255)
    @NotEmpty(message = "Email cannot be blank.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotEmpty(message = "Password cannot be blank.")
    private String password;
}
