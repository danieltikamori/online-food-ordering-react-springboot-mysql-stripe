/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import me.amlu.model.Category;
import me.amlu.model.IngredientsItems;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
public class CreateFoodRequest {

    @Size(max = 255)
    @NotEmpty(message = "Name cannot be blank.")
    private String name;

    @Size(max = 2047)
    private String description;

    @Positive(message = "Price must be a positive number.")
    private BigDecimal price;

    @NotEmpty(message = "Category cannot be empty.")
    private Category category;

    @NotEmpty(message = "Images cannot be empty.")
    private List<String> images;

    @Positive
    private Long restaurantId;

    private boolean isVegetarian;
    private boolean isSeasonal;

    private Set<IngredientsItems> ingredients;

}
