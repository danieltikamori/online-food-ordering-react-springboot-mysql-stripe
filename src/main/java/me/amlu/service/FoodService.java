/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.service;

import me.amlu.dto.FoodDto;
import me.amlu.model.Category;
import me.amlu.model.Food;
import me.amlu.model.Restaurant;
import me.amlu.request.CreateFoodRequest;
import me.amlu.request.UpdateFoodRequest;
import me.amlu.service.exceptions.FoodNotFoundException;

import java.util.List;

public interface FoodService {

    Food createFood(CreateFoodRequest createFoodRequest, Category category, Restaurant restaurant) throws Exception;
    void updateFood(Long foodId, UpdateFoodRequest request) throws Exception;

    void deleteFood(Long foodId, Long userId) throws Exception;

    List<Food> getRestaurantsFood(Long restaurantId,
                                  boolean isVegetarian,
                                  boolean isNonVegetarian,
                                  boolean isSeasonal,
                                  String foodCategory) throws Exception;

    List<Food> searchFood(String keyword) throws Exception;
    Food findFoodById(Long foodId) throws FoodNotFoundException;
//    List<Food> findFoodByCategory(Category category) throws Exception;
//    List<Food> getAllFoods();

    Food updateAvailabilityStatus(Long foodId) throws Exception;

    FoodDto getFoodIngredients(Long restaurantId, Long foodId) throws Exception;
}
