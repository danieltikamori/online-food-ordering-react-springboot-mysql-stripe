package me.amlu.config;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StrongPasswordValidator.class)
public @interface StrongPassword {
    String message() default "Password must meet complexity requirements";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}