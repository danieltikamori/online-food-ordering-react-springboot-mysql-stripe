package me.amlu.request;

import lombok.Data;

@Data
public class IngredientRequest {


    private String itemName;

    private Long categoryId;

    private Long restaurantId;
}
