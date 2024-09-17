/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class IngredientRequest {

    @NotEmpty(message = "Name cannot be blank.")
    private String itemName;

    @Positive(message = "Category must be a positive number.")
    private Long categoryId;

    @Positive(message = "RestaurantId must be a positive number.")
    private Long restaurantId;
}
