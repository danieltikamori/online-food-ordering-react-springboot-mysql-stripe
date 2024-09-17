/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import me.amlu.model.Address;

@Data
public class OrderRequest {

    @Positive
    private Long restaurantId;

    @NotEmpty(message = "Idempotency key cannot be blank.")
    private String idempotencyKey;

    @NotEmpty(message = "Delivery address cannot be empty.")
    private Address deliveryAddress;

}
