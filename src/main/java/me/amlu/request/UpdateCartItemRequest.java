/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import me.amlu.model.User;

import java.time.Instant;

@Data
public class UpdateCartItemRequest {

    @Positive
    private Long cartItemId;

    @Positive
    private Long foodId;

    @Positive(message = "Quantity must be a positive number.")
    private int quantity;

   @NotEmpty
    private Instant updatedAt;

    @NotEmpty
    private User updatedBy;

}
