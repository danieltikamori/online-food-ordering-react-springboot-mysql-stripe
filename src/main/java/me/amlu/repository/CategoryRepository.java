package me.amlu.repository;

import me.amlu.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    public Category findByCategoryName(String categoryName);

    public List<Category> findCategoryByRestaurantId(Long restaurantId);

    public Category findCategoryById(Long categoryId);

    @Query("SELECT c FROM Category c WHERE LOWER(c.categoryName) = LOWER(:categoryName)")
    Category findSimilarCategory(@Param("categoryName") String categoryName);

    List<Category> findByRestaurantId(Long id);
}