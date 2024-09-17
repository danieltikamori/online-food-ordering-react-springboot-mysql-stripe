/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import me.amlu.model.CartItem;
@Data
public class AddToCartRequest {

    @NotEmpty(message = "Token cannot be blank.")
    private String token;

    @NotEmpty(message = "Cart item cannot be null.")
    private CartItem cartItem;

}