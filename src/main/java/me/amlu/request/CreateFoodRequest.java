package me.amlu.request;

import lombok.Data;
import me.amlu.model.Category;
import me.amlu.model.IngredientsItems;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateFoodRequest {

    private String name;
    private String description;
    private BigDecimal price;

    private Category category;
    private List<String> images;

    private Long restaurantId;
    private boolean isVegetarian;
    private boolean isSeasonal;

    private List<IngredientsItems> ingredients;

}
