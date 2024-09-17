/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Order creation failed")
public class OrderCreationException extends RuntimeException {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;

    public OrderCreationException(String message) {
        super(message);
    }
}
