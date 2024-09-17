/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.config;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SensitiveDataValidator.class)
public @interface SensitiveData {
    String[] rolesAllowed() default {}; // Roles allowed to see this data

    String message() default "Unauthorized access to sensitive data.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}