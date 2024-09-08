package me.amlu.repository;

import me.amlu.model.IngredientCategory;
import me.amlu.model.IngredientsItems;
import me.amlu.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IngredientsItemsRepository extends JpaRepository<IngredientsItems, Long> {

    List<IngredientsItems> findByRestaurantId(Long restaurantId);

    // Specific method for uniqueness check
    Optional<IngredientsItems> findByIngredientNameAndIngredientCategoryAndRestaurant(String ingredientName,
                                                                                      IngredientCategory ingredientCategory, Restaurant restaurant);

    boolean existsByIngredientNameAndIngredientCategoryAndRestaurant(String ingredientName,
                                                                     IngredientCategory ingredientCategory,
                                                                     Restaurant restaurant);

    boolean existsByIngredientNameAndIngredientCategoryIdAndRestaurantId(String ingredientName, Long ingredientCategoryId, Long restaurantId);
}
