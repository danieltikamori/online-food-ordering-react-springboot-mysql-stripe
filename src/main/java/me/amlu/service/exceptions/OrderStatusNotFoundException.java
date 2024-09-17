/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Order status not found")
public class OrderStatusNotFoundException extends Exception {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;

    public OrderStatusNotFoundException(String message) {

        super(message);
    }
}
