package me.amlu.repository;

import me.amlu.model.Category;
import me.amlu.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {

    List<Food> findByRestaurantId(Long restaurantId);

//    @Query("SELECT f FROM Food f WHERE f.name LIKE %:keyword% OR f.foodCategory.categoryName LIKE %:keyword%")
    @Query("SELECT f FROM Food f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR f.foodCategory.categoryName LIKE %:keyword%")
    List<Food> searchFood(@Param("keyword") String keyword);

    @Query("SELECT f FROM Food f WHERE f.foodCategory.categoryName = :categoryName")
    List<Food> findByFoodCategory(@Param("categoryName") Category categoryName);
}
