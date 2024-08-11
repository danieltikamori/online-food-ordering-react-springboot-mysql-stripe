package me.amlu.request;

import lombok.Data;

@Data
public class IngredientCategoryRequest {

    private String categoryName;


    private Long restaurantId;
}
