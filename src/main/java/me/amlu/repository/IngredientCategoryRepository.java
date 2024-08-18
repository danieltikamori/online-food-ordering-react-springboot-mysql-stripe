package me.amlu.repository;

import me.amlu.model.IngredientCategory;
import me.amlu.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IngredientCategoryRepository extends JpaRepository<IngredientCategory, Long> {

    List<IngredientCategory> findByRestaurantId(Long restaurantId);
    Optional<IngredientCategory> findByCategoryNameAndRestaurantId(String categoryName, Long restaurantId);

    IngredientCategory findByCategoryNameAndRestaurant(String categoryName, Restaurant restaurant);


}
