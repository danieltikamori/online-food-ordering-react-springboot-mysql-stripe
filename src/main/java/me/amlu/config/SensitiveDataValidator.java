/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.config;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SensitiveDataValidator implements ConstraintValidator<SensitiveData, Object> {
    private String[] rolesAllowed;

    @Override
    public void initialize(SensitiveData constraintAnnotation) {
        this.rolesAllowed = constraintAnnotation.rolesAllowed();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        // 1. Get the authenticated user's roles
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false; // Not authenticated - deny access
        }
        // 2. Check if the user has any of the allowed roles
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            for (String role : rolesAllowed) {
                if (authority.getAuthority().equals("ROLE_" + role)) {
                    return true; // User has an allowed role - allow access
                }
            }
        }
        // 3. If no allowed role is found, return false (deny access)
        return false;
    }
}