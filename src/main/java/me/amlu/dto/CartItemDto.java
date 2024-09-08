package me.amlu.dto;

import java.util.List;

public class CartItemDto {
    private Long foodId;
    private int quantity;
    private List<IngredientItemDto> ingredients;
}

