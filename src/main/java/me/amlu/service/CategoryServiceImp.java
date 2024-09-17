/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.service;

import lombok.NonNull;
import me.amlu.model.Category;
import me.amlu.model.Restaurant;
import me.amlu.model.User;
import me.amlu.repository.CategoryRepository;
import me.amlu.service.exceptions.CategoryNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static me.amlu.common.SecurityUtil.getAuthenticatedUser;

@Service
public class CategoryServiceImp implements CategoryService {

    private final EntityUniquenessService uniquenessService;

    private final RestaurantService restaurantService;

    private final CategoryRepository categoryRepository;

    public CategoryServiceImp(EntityUniquenessService uniquenessService, RestaurantService restaurantService, CategoryRepository categoryRepository) {
        this.uniquenessService = uniquenessService;
        this.restaurantService = restaurantService;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public Category createCategory(@NonNull String categoryName, Long userId) throws Exception {

        Optional<Restaurant> restaurant = restaurantService.getRestaurantsByUserId(userId);
        System.out.println("categoryName: " + categoryName);
        System.out.println("restaurant: " + restaurant);

        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setRestaurant(restaurant.orElse(null));
        category.setCreatedAt(Instant.now());
        category.setUpdatedAt(Instant.now());
        category.setCreatedBy(getAuthenticatedUser());
        category.setUpdatedBy(getAuthenticatedUser());
        category.setDeletedAt(null);
        category.setDeletedBy(null);

        uniquenessService.checkUniqueCategory(category); // Check uniqueness

        return categoryRepository.save(category);
    }

    @Override
    public List<Category> findCategoryByRestaurantId(Long id) throws Exception {

//        Optional<Restaurant> restaurant = restaurantService.getRestaurantsByUserId(userId);
//        return categoryRepository.findCategoryByRestaurantId(restaurant.map(Restaurant::getCategory_id).orElse(null));
//        Optional<Restaurant> restaurant = Optional.ofNullable(restaurantService.findRestaurantById(category_id));
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

        return categoryRepository.findByCategoryName(categoryName);

        //As this check may cause errors when checking for duplicate category names.
//        Optional<Category> optionalCategory = Optional.ofNullable(categoryRepository.findByCategoryName(categoryName));
//        if (optionalCategory.isEmpty()) {
//            throw new CategoryNotFoundException("Category not found.");
//        }
//        return optionalCategory.get();
    }

    @Override
    public Category findSimilarCategory(String categoryName) {
        return categoryRepository.findSimilarCategoryIgnoreCase(categoryName);
    }

    @Override
    @Transactional
    public Category updateCategory(Long categoryId, @NonNull String categoryName, Long userId) throws Exception {

        Category category = findCategoryById(categoryId);
        category.setCategoryName(categoryName);
        category.setUpdatedAt(Instant.now());
        category.setUpdatedBy(getAuthenticatedUser());

        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) throws Exception {

        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isEmpty()) {
            throw new CategoryNotFoundException("Category not found.");
        }
        categoryRepository.deleteById(categoryId);
        optionalCategory.get().setDeletedAt(Instant.now());
        optionalCategory.get().setDeletedBy(getAuthenticatedUser());
    }
}
