/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.dto;

import lombok.Data;
import me.amlu.model.Food;
import me.amlu.model.IngredientsItems;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class CartItemDto {

    Food food;
    private Long foodId;
    private String foodName;
    private BigDecimal foodPrice;
    private int quantity;
    private Set<IngredientsItems> ingredients;
    private Set<Long> ingredientsIds;

}

