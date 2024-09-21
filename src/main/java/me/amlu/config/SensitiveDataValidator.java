/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
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