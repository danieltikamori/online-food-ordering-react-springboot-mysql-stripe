package me.amlu.request;

import lombok.Data;
import me.amlu.service.UniqueEntity;

@Data
public class IngredientRequest {


    @UniqueEntity(fields = {"itemName", "categoryId", "restaurantId"})
    private String itemName;

    private Long categoryId;

    private Long restaurantId;
}
