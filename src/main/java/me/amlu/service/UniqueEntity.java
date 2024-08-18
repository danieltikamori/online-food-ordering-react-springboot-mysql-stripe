package me.amlu.service;

import javax.validation.Constraint;
import javax.validation.Payload;
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