package me.amlu.repository;

import me.amlu.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    public List<Category> findCategoryByRestaurantId(Long restaurantId);

    public Category findCategoryById(Long categoryId);
}
