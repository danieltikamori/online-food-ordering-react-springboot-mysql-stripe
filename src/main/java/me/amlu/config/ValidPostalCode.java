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
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPostalCodeValidator.class)
public @interface ValidPostalCode {
    String message() default "Invalid postal code format.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
