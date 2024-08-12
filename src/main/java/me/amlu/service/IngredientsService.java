package me.amlu.service;

import me.amlu.model.IngredientCategory;
import me.amlu.model.IngredientsItems;

import java.util.List;

public interface IngredientsService {

    public IngredientCategory createIngredientCategory(String name, Long restaurantId) throws Exception;

    public IngredientCategory findIngredientCategoryById(Long id) throws Exception;

    public List<IngredientCategory> findIngredientCategoryByRestaurantId(Long restaurantId) throws Exception;

    public IngredientsItems createIngredientsItems(Long restaurantId, String ingredientName, Long ingredientCategoryId) throws Exception;

    public List<IngredientsItems> findRestaurantsIngredients(Long restaurantId) throws Exception;

    public IngredientsItems updateStock(Long id) throws Exception;

    public IngredientCategory updateIngredientCategory(Long id, String name) throws Exception;

    public void deleteIngredientCategory(Long id) throws Exception;

    public void deleteIngredientsItems(Long id) throws Exception;
}
