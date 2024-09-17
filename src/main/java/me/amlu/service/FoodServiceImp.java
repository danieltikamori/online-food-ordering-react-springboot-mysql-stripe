/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import me.amlu.dto.FoodDto;
import me.amlu.dto.IngredientItemDto;
import me.amlu.model.*;
import me.amlu.repository.FoodRepository;
import me.amlu.repository.UserRepository;
import me.amlu.request.CreateFoodRequest;
import me.amlu.request.UpdateFoodRequest;
import me.amlu.service.exceptions.FoodNotFoundException;
import me.amlu.service.exceptions.RestaurantNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static me.amlu.common.SecurityUtil.getAuthenticatedUser;

@Service
@Validated
public class FoodServiceImp implements FoodService {

    private final EntityUniquenessService uniquenessService;
    private static final Logger log = LogManager.getLogger(FoodServiceImp.class);

    private final FoodRepository foodRepository;
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;


    public FoodServiceImp(EntityUniquenessService uniquenessService, FoodRepository foodRepository, UserRepository userRepository) {
        this.uniquenessService = uniquenessService;
        this.foodRepository = foodRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Food createFood(@Valid CreateFoodRequest createFoodRequest, Category category, Restaurant restaurant) throws Exception {

        Food food = new Food();
        food.setFoodCategory(category);
        food.setRestaurant(restaurant);

        Set<IngredientsItems> ingredients = createFoodRequest.getIngredients();
        Set<IngredientsItems> mergedIngredients = Collections.synchronizedSet(new LinkedHashSet<>());
        for (IngredientsItems ingredient : ingredients) {
            // Have an EntityManager injected
            mergedIngredients.add(entityManager.merge(ingredient));
        }
        food.setIngredients(mergedIngredients);

        food.setName(createFoodRequest.getName());
        food.setDescription(createFoodRequest.getDescription());
        food.setPrice(createFoodRequest.getPrice());
        food.setImages(createFoodRequest.getImages());
//        food.setIngredients(createFoodRequest.getIngredients());

        food.setSeasonal(createFoodRequest.isSeasonal());
        food.setVegetarian(createFoodRequest.isVegetarian());
        food.setCreatedAt(Instant.now());
        food.setUpdatedAt(Instant.now());
        food.setCreatedBy(getAuthenticatedUser());
        food.setUpdatedBy(getAuthenticatedUser());


        uniquenessService.checkUniqueFood(food); // Check if food name already exists
        Food savedFood = foodRepository.save(food);
        restaurant.getFoods().add(savedFood);

        return savedFood;
    }

    @Transactional
    public void updateFood(Long foodId, @Valid UpdateFoodRequest request) throws Exception {

        int maxRetries = 3; // Define a maximum number of retry attempts
        int retryCount = 0;

        while (retryCount < maxRetries) {
            try {
                Food existingFood = foodRepository.findById(foodId)
                        .orElseThrow(() -> new FoodNotFoundException("Food not found"));

                existingFood.setName(request.getName());
                existingFood.setDescription(request.getDescription());
                existingFood.setFoodCategory(request.getFoodCategory());
                existingFood.setPrice(request.getPrice());
                existingFood.setImages(request.getImages());
                existingFood.setAvailable(request.isAvailable());
                existingFood.setSeasonal(request.isSeasonal());
                existingFood.setVegetarian(request.isVegetarian());
                existingFood.setIngredients(request.getIngredients());
                existingFood.setUpdatedAt(Instant.now());
                existingFood.setUpdatedBy(getAuthenticatedUser());

                foodRepository.save(existingFood);

                return; // Exit the loop if the update is successful

            } catch (OptimisticLockException e) {
                retryCount++;
                if (retryCount >= maxRetries) {
                    throw new ConcurrentModificationException("Food with ID " + foodId +
                            " has been concurrently modified. Please try again later.", e);
                }
                // Log the retry attempt (for debugging)
                log.warn("OptimisticLockException caught. Retrying update for food ID: {}, attempt {}/{}",
                        foodId, retryCount, maxRetries);

                // You might want to add a small delay here (e.g., Thread.sleep(500);)
                // to reduce the likelihood of hitting the same conflict repeatedly.
                // Sleep for a short time before retrying
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    // Handle the interrupted exception
                    Thread.currentThread().interrupt();
                }
            } catch (FoodNotFoundException e) {
                throw new FoodNotFoundException("Food not found.");
            }
        }
    }


    @Override
    @Transactional
    public void deleteFood(Long foodId, Long userId) throws Exception {
        User user = getAuthenticatedUser();

        Food food;
        try {
            food = findFoodById(foodId);
            food.setDeletedAt(Instant.now());
            food.setDeletedBy(getAuthenticatedUser());

        } catch (FoodNotFoundException e) {
            throw new FoodNotFoundException("Food not found.");
        }

//        foodRepository.delete(food);
        foodRepository.save(food);

    }

    @Override
    public List<Food> getRestaurantsFood(Long restaurantId,
                                         boolean isVegetarian,
                                         boolean isNonVegetarian,
                                         boolean isSeasonal,
                                         String foodCategory) {

        List<Food> foodList = foodRepository.findByRestaurantId(restaurantId);

        if (isVegetarian) {
            foodList = filterByVegetarian(foodList, isVegetarian);
        }
        if (isNonVegetarian) {
            foodList = filterByNonVegetarian(foodList, isNonVegetarian);
        }
        if (isSeasonal) {
            foodList = filterBySeasonal(foodList, isSeasonal);
        }
        if (foodCategory != null && !foodCategory.isEmpty()) {
            foodList = filterByCategory(foodList, foodCategory);
        }

        return foodList;
    }

    private List<Food> filterByCategory(List<Food> foodList, String foodCategory) {
        return foodList.stream().filter(food -> {
            if (food.getFoodCategory() != null) {
                return food.getFoodCategory().getCategoryName().equals(foodCategory);
            }
            return false;
        }).collect(Collectors.toList());
    }

    private List<Food> filterBySeasonal(List<Food> foodList, boolean isSeasonal) {
        return foodList.stream().filter(food -> food.isSeasonal() == isSeasonal).collect(Collectors.toList());
    }

    private List<Food> filterByNonVegetarian(List<Food> foodList, boolean isNonVegetarian) {
        return foodList.stream().filter(food -> !food.isVegetarian()).collect(Collectors.toList());
    }

    private List<Food> filterByVegetarian(List<Food> foodList, boolean isVegetarian) {
        return foodList.stream().filter(food -> food.isVegetarian() == isVegetarian).collect(Collectors.toList());
    }

    @Override
    public List<Food> searchFood(String keyword) {
        return foodRepository.searchFoodIgnoreCase(keyword);
    }

    @Override
    @Cacheable(value = "foods", key = "#foodId")
    public Food findFoodById(Long foodId) throws FoodNotFoundException {
        Optional<Food> optionalFood = foodRepository.findById(foodId);
        if (optionalFood.isEmpty()) {
            throw new FoodNotFoundException("Food not exist.");
        }
        return optionalFood.get();
    }

//    @Override
//    public List<Food> findFoodByCategory(Category category) throws Exception {
//
//        try {
//            return foodRepository.findByFoodCategory(category);
//        } catch (Exception e) {
//            throw new CategoryNotFoundException("Category not found.");
//        }
//
//    }
//
//    @Override
//    public List<Food> getAllFoods() {
//        return foodRepository.findAll();
//    }

    @Override
    @Transactional
    public Food updateAvailabilityStatus(Long foodId) throws Exception {

        try {
            Food food = findFoodById(foodId);
            food.setAvailable(!food.isAvailable());
            food.setUpdatedAt(Instant.now());
            food.setUpdatedBy(getAuthenticatedUser());
            return foodRepository.save(food);
        } catch (FoodNotFoundException e) {
            throw new FoodNotFoundException("Food not found.");
        }
    }

    @Override
    public FoodDto getFoodIngredients(Long restaurantId, Long foodId) throws Exception {

        Food food = findFoodById(foodId);
        if (!food.getRestaurant().getRestaurant_id().equals(restaurantId)) {
            throw new RestaurantNotFoundException("Restaurant not found.");
        }

        // Force lazy loading of ingredients
        Hibernate.initialize(food.getIngredients());

        // Create and populate the DTO
        FoodDto foodDto = new FoodDto();
        foodDto.setFood_id(food.getFood_id());
        foodDto.setName(food.getName());
        // ... map other fields from food to foodDto ...

        // Map ingredients to IngredientItemDto
        Set<IngredientItemDto> ingredientDtos = food.getIngredients().stream()
                .map(ingredient -> {
                    IngredientItemDto ingredientDto = new IngredientItemDto();
                    ingredientDto.setId(ingredient.getIngredients_items_id());
                    ingredientDto.setIngredientName(ingredient.getIngredientName());
                    ingredientDto.setIngredientCategory(ingredient.getIngredientCategory());
                    ingredientDto.setRestaurant(ingredient.getRestaurant());
                    ingredientDto.setInStock(ingredient.isInStock());
                    return ingredientDto;
                })
                .collect(Collectors.toSet());
        foodDto.setIngredients(ingredientDtos);
        return foodDto;
    }
}
