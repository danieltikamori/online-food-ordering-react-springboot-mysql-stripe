package me.amlu.request;

import lombok.Data;
import me.amlu.service.UniqueEntity;

@Data
public class IngredientCategoryRequest {

    @UniqueEntity(fields = {"categoryName", "restaurantId"})
    private String categoryName;


    private Long restaurantId;
}
