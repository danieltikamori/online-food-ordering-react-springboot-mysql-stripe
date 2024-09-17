/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.repository;

import me.amlu.model.IngredientCategory;
import me.amlu.model.IngredientsItems;
import me.amlu.model.Restaurant;

import java.util.Optional;
import java.util.Set;

public interface IngredientsItemsRepository extends BaseRepository<IngredientsItems, Long> {

    Set<IngredientsItems> findByRestaurantId(Long restaurantId);

    // Specific method for uniqueness check
    Optional<IngredientsItems> findByIngredientNameAndIngredientCategoryAndRestaurant(String ingredientName,
                                                                                                        IngredientCategory ingredientCategory, Restaurant restaurant);

    boolean existsByIngredientNameAndIngredientCategoryAndRestaurant(String ingredientName,
                                                                                       IngredientCategory ingredientCategory,
                                                                                       Restaurant restaurant);

    boolean existsByIngredientNameAndIngredientCategoryIdAndRestaurantId(String ingredientName, Long ingredientCategoryId, Long restaurantId);
}
