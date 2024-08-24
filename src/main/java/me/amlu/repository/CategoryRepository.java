package me.amlu.repository;

import me.amlu.model.Category;
import me.amlu.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    public Category findByCategoryName(String categoryName);

    public List<Category> findCategoryByRestaurantId(Long restaurantId);

    public Category findCategoryById(Long categoryId);

    List<Category> findByRestaurantId(Long id);

    Category findByCategoryNameAndRestaurant(String categoryName, Restaurant restaurant);

    @Query("SELECT c FROM Category c WHERE LOWER(c.categoryName) = LOWER(:categoryName)")
    Category findSimilarCategory(@Param("categoryName") String categoryName);

    boolean existsByCategoryNameAndRestaurant(String categoryName, Restaurant restaurant);

    boolean existsByCategoryNameAndRestaurantId(String categoryName, Long restaurantId);

}