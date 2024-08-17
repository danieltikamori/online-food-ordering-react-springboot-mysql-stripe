package me.amlu.service;

import me.amlu.model.Category;

import java.util.List;

public interface CategoryService {

    public Category createCategory(String categoryName, Long userId) throws Exception;

    public List<Category> findCategoryByRestaurantId(Long restaurantId) throws Exception;

    public Category findCategoryById(Long categoryId) throws Exception;

    public Category findCategoryByName(String categoryName) throws Exception;

    public Category updateCategory(Long categoryId, String categoryName, Long userId) throws Exception;

    public void deleteCategory(Long categoryId) throws Exception;

}
