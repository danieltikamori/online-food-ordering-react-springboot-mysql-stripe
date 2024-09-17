package me.amlu.repository;

import me.amlu.model.IngredientCategory;
import me.amlu.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface IngredientCategoryRepository extends BaseRepository<IngredientCategory, Long> {

    List<IngredientCategory> findByRestaurantId(Long restaurantId);
    
    Optional<IngredientCategory> findByCategoryNameAndRestaurantId(String categoryName, Long restaurantId);

    IngredientCategory findByCategoryNameAndRestaurant(String categoryName, Restaurant restaurant);

    boolean existsByCategoryNameAndRestaurant(String categoryName, Restaurant restaurant);

    boolean existsByCategoryNameAndRestaurantId(String categoryName, Long restaurantId);
}
