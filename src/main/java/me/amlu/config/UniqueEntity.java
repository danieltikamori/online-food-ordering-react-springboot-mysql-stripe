/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.config;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEntityValidator.class)
public @interface UniqueEntity {
    String message() default "Entity with these attributes already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    // Attributes to check for uniqueness (e.g., "name", "restaurantId")
    String[] fields();
}