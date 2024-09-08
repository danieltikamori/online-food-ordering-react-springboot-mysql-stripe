package me.amlu.request;

import lombok.Data;
import me.amlu.service.UniqueEntity;

@Data
public class IngredientRequest {

    private String itemName;

    private Long categoryId;

    private Long restaurantId;
}
