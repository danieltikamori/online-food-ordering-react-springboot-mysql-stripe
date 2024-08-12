package me.amlu.service;

import me.amlu.model.Category;
import me.amlu.model.Restaurant;
import me.amlu.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImp implements CategoryService {

    private final RestaurantService restaurantService;

    private final CategoryRepository categoryRepository;

    public CategoryServiceImp(RestaurantService restaurantService, CategoryRepository categoryRepository) {
        this.restaurantService = restaurantService;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createCategory(String categoryName, Long userId) throws Exception {

        Optional<Restaurant> restaurant = restaurantService.getRestaurantsByUserId(userId);
        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setRestaurant(restaurant.orElse(null));

        return categoryRepository.save(category);
    }

    @Override
    public List<Category> findCategoryByRestaurantId(Long userId) throws Exception {

        Optional<Restaurant> restaurant = restaurantService.getRestaurantsByUserId(userId);
        return categoryRepository.findCategoryByRestaurantId(restaurant.map(Restaurant::getId).orElse(null));

    }

    @Override
    public Category findCategoryById(Long categoryId) throws Exception {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isEmpty()) {
        throw new CategoryNotFoundException("Category not found.");
        }
            return optionalCategory.get();
    }

    @Override
    public void deleteCategory(Long categoryId) throws Exception {

        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isEmpty()) {
            throw new CategoryNotFoundException("Category not found.");
        }
        categoryRepository.deleteById(categoryId);

    }
}
