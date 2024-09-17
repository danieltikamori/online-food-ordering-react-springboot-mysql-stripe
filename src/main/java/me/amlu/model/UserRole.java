/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.model;

/**
 * @author Daniel Itiro Tikamori
 * Enum Plural naming canvention: UpperCamelCase, e.g., EntityNameRole (UserRole)
 * Singular naming canvention(constant with single value): UPPER_SNAKE_CASE e.g., ROLE_CUSTOMER
 */

public enum UserRole {

    ROLE_CUSTOMER,
    ROLE_RESTAURANT_OWNER,
    ROLE_RESTAURANT_STAFF,
    ROLE_DELIVERY_PERSON,

    ROLE_ROOT,
    ROLE_ADMIN

}
