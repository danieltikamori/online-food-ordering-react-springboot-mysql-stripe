/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import me.amlu.model.Category;
import me.amlu.model.IngredientsItems;
import me.amlu.model.User;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
public class UpdateFoodRequest {

    @NotEmpty(message = "Name cannot be blank.")
    private String name;

    private String description;

    @NotEmpty(message = "Category cannot be empty.")
    private Category foodCategory;

    @Positive(message = "Price must be a positive number.")
    private BigDecimal price;

    private List<String> images;

    private boolean isAvailable;
    private boolean isVegetarian;
    private boolean isSeasonal;

    private Set<IngredientsItems> ingredients;

    @NotEmpty
    private Instant updatedAt;

    @NotEmpty
    private User updatedBy;

}
