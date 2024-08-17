package me.amlu.service;

import me.amlu.model.Category;
import me.amlu.model.Restaurant;
import me.amlu.repository.CategoryRepository;
import me.amlu.service.Exceptions.CategoryNotFoundException;
import me.amlu.service.Exceptions.DuplicateCategoryException;
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
        Category existingCategory = categoryRepository.findByCategoryName(categoryName);
        if (existingCategory != null) {
            // You could throw an exception or return an error message here
            throw new DuplicateCategoryException("Category with name '" + categoryName + "' already exists");
        }
        Optional<Restaurant> restaurant = restaurantService.getRestaurantsByUserId(userId);
        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setRestaurant(restaurant.orElse(null));

        return categoryRepository.save(category);
    }

    @Override
    public List<Category> findCategoryByRestaurantId(Long id) throws Exception {

//        Optional<Restaurant> restaurant = restaurantService.getRestaurantsByUserId(userId);
//        return categoryRepository.findCategoryByRestaurantId(restaurant.map(Restaurant::getId).orElse(null));
                Optional<Restaurant> restaurant = restaurantService.getRestaurantsByUserId(id);
        return categoryRepository.findByRestaurantId(id);

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
    public Category findCategoryByName(String categoryName) throws Exception {

        Optional<Category> optionalCategory = Optional.ofNullable(categoryRepository.findByCategoryName(categoryName));
        if (optionalCategory.isEmpty()) {
            throw new CategoryNotFoundException("Category not found.");
        }
        return optionalCategory.get();
    }

    @Override
    public Category findSimilarCategory(String categoryName) {
        return categoryRepository.findSimilarCategory(categoryName);
    }

    @Override
    public Category updateCategory(Long categoryId, String categoryName, Long userId) throws Exception {

        Category category = findCategoryById(categoryId);
        category.setCategoryName(categoryName);
        return categoryRepository.save(category);
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
