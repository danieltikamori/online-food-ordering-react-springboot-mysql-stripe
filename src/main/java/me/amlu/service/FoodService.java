package me.amlu.service;

import me.amlu.dto.FoodDto;
import me.amlu.model.Category;
import me.amlu.model.Food;
import me.amlu.model.Restaurant;
import me.amlu.request.CreateFoodRequest;

import java.util.List;

public interface FoodService {

    public Food createFood(CreateFoodRequest createFoodRequest, Category category, Restaurant restaurant) throws Exception;

    void deleteFood(Long foodId) throws Exception;

    public List<Food> getRestaurantsFood(Long restaurantId,
                                         boolean isVegetarian,
                                         boolean isNonVegetarian,
                                         boolean isSeasonal,
                                         String foodCategory) throws Exception;

    public List<Food> searchFood(String keyword) throws Exception;
    public Food findFoodById(Long foodId) throws Exception;
//    public List<Food> findFoodByCategory(Category category) throws Exception;
//    public List<Food> getAllFoods();

    public Food updateAvailabilityStatus(Long foodId) throws Exception;

    FoodDto getFoodIngredients(Long restaurantId, Long foodId) throws Exception;
}
