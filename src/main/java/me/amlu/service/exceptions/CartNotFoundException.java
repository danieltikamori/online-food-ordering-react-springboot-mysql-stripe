/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus( value = HttpStatus.NOT_FOUND, reason = "Cart not found")
public class CartNotFoundException extends Exception {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;

    public CartNotFoundException(String message) {

        super(message);
    }
}
