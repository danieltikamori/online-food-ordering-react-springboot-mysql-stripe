/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Restaurant not found")
public class RestaurantNotFoundException extends Exception {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;

    public RestaurantNotFoundException(String message) {
        super(message);
    }
}
