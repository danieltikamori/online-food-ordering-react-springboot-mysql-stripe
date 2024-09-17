/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class IngredientCategoryRequest {

    @NotEmpty(message = "Name cannot be blank.")
    private String categoryName;

    @Positive
    private Long restaurantId;
}
