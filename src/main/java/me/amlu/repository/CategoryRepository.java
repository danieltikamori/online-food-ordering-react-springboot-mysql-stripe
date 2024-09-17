/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.repository;

import me.amlu.model.Category;
import me.amlu.model.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends BaseRepository<Category, Long> {

    Category findByCategoryName(String categoryName);

    List<Category> findCategoryByRestaurantId(Long restaurantId);

    Category findCategoryById(Long categoryId);

    List<Category> findByRestaurantId(Long id);

    Category findByCategoryNameAndRestaurant(String categoryName, Restaurant restaurant);

    @Query("SELECT c FROM Category c WHERE LOWER(c.categoryName) = LOWER(:categoryName) AND c.deletedAt IS NULL")
    Category findSimilarCategoryIgnoreCase(@Param("categoryName") String categoryName);

    boolean existsByCategoryNameAndRestaurant(String categoryName, Restaurant restaurant);

    boolean existsByCategoryNameAndRestaurantId(String categoryName, Long restaurantId);

}