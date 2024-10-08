/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
 */

package me.amlu.service;

import lombok.NonNull;
import me.amlu.model.IngredientCategory;
import me.amlu.model.IngredientsItems;
import me.amlu.model.Restaurant;
import me.amlu.repository.IngredientCategoryRepository;
import me.amlu.repository.IngredientsItemsRepository;
import me.amlu.service.exceptions.IngredientCategoryNotFoundException;
import me.amlu.service.exceptions.IngredientsItemsNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static me.amlu.common.SecurityUtil.getAuthenticatedUser;

@Service
public class IngredientsServiceImp implements IngredientsService {

    private final EntityUniquenessService uniquenessService;

    private final IngredientsItemsRepository ingredientsItemsRepository;

    private final IngredientCategoryRepository ingredientCategoryRepository;

    private final RestaurantService restaurantService;

    public IngredientsServiceImp(EntityUniquenessService uniquenessService, IngredientsItemsRepository ingredientsItemsRepository, IngredientCategoryRepository ingredientCategoryRepository, RestaurantService restaurantService) {
        this.uniquenessService = uniquenessService;
        this.ingredientsItemsRepository = ingredientsItemsRepository;
        this.ingredientCategoryRepository = ingredientCategoryRepository;
        this.restaurantService = restaurantService;
    }

    @Override
    @Transactional
    public IngredientCategory createIngredientCategory(@NonNull String categoryName, @NonNull Long restaurantId) throws Exception {

        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);

        // Check if a category with the same name already exists for this restaurant
//        Optional<IngredientCategory> existingCategory = ingredientCategoryRepository.findByCategoryNameAndRestaurantId(categoryName, restaurantId);
//        if (existingCategory.isPresent()) {
//            throw new DuplicateCategoryException("Category with name '" + categoryName + "' already exists for this restaurant.");
//        }
//        if (uniquenessService.isEntityUnique(new IngredientCategory(), "categoryName", "restaurant")) {
//            throw new DuplicateCategoryException("Ingredient category name '" + categoryName + "' already exists");
//        }

        IngredientCategory ingredientCategory = new IngredientCategory();
        ingredientCategory.setCategoryName(categoryName);
        ingredientCategory.setRestaurant(restaurant);
        ingredientCategory.setCreatedAt(Instant.now());
        ingredientCategory.setUpdatedAt(Instant.now());
        ingredientCategory.setCreatedBy(getAuthenticatedUser());
        ingredientCategory.setUpdatedBy(getAuthenticatedUser());
        ingredientCategory.setDeletedAt(null);
        ingredientCategory.setDeletedBy(null);


        uniquenessService.checkUniqueIngredientCategory(ingredientCategory); // Check uniqueness

        return ingredientCategoryRepository.save(ingredientCategory);

    }

    @Override
    public IngredientCategory findIngredientCategoryById(Long id) throws Exception {

        Optional<IngredientCategory> optionalIngredientCategory = ingredientCategoryRepository.findById(id);
        if (optionalIngredientCategory.isPresent()) {
            return optionalIngredientCategory.get();
        } else {
            throw new IngredientCategoryNotFoundException("Ingredient category not found.");
        }

    }

    @Override
    public List<IngredientCategory> findIngredientCategoryByRestaurantId(Long restaurantId) throws Exception {

        restaurantService.findRestaurantById(restaurantId);

        return ingredientCategoryRepository.findByRestaurantId(restaurantId);
    }

    @Override
    @Transactional
    public IngredientsItems createIngredientsItems(@NonNull Long restaurantId, @NonNull String ingredientName, @NonNull Long ingredientCategoryId) throws Exception {

        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        IngredientCategory ingredientCategory = findIngredientCategoryById(ingredientCategoryId);

//        if (uniquenessService.isEntityUnique(new IngredientsItems(), "ingredientName", "ingredientCategory", "restaurant")) {
//            throw new DuplicateItemException("Ingredient item with name '" + ingredientName +
//                    "' already exists in this category.");
//        }

        IngredientsItems ingredientsItems = new IngredientsItems();
        ingredientsItems.setIngredientName(ingredientName);
        ingredientsItems.setIngredientCategory(ingredientCategory);
        ingredientsItems.setRestaurant(restaurant);
        ingredientsItems.setInStock(true);
        ingredientsItems.setCreatedAt(Instant.now());
        ingredientsItems.setUpdatedAt(Instant.now());
        ingredientsItems.setCreatedBy(getAuthenticatedUser());
        ingredientsItems.setUpdatedBy(getAuthenticatedUser());
        ingredientsItems.setDeletedAt(null);
        ingredientsItems.setDeletedBy(null);

        uniquenessService.checkUniqueIngredientItem(ingredientsItems); // Check uniqueness
        return ingredientsItemsRepository.save(ingredientsItems);

    }

    @Override
    public Set<IngredientsItems> findRestaurantsIngredients(Long restaurantId) throws Exception {

        return ingredientsItemsRepository.findByRestaurantId(restaurantId);
    }

    @Override
    @Transactional
    public IngredientsItems updateStock(@NonNull Long id) throws Exception {

        Optional<IngredientsItems> optionalIngredientsItems = ingredientsItemsRepository.findById(id);
        if (optionalIngredientsItems.isEmpty()) {
            throw new IngredientsItemsNotFoundException("Ingredient not found.");
        }
        IngredientsItems ingredientsItems = optionalIngredientsItems.get();
        ingredientsItems.setInStock(!ingredientsItems.isInStock());
        ingredientsItems.setUpdatedAt(Instant.now());
        ingredientsItems.setUpdatedBy(getAuthenticatedUser());

        return ingredientsItemsRepository.save(ingredientsItems);
    }

    @Override
    @Transactional
    public IngredientCategory updateIngredientCategory(@NonNull Long id, @NonNull String name) throws Exception {

        IngredientCategory ingredientCategory = findIngredientCategoryById(id);
        ingredientCategory.setCategoryName(name);
        ingredientCategory.setUpdatedAt(Instant.now());
        ingredientCategory.setUpdatedBy(getAuthenticatedUser());

        uniquenessService.checkUniqueIngredientCategory(ingredientCategory); // Check uniqueness

        return ingredientCategoryRepository.save(ingredientCategory);
    }

    @Override
    @Transactional
    public void deleteIngredientCategory(Long id) throws Exception {

        IngredientCategory ingredientCategory = findIngredientCategoryById(id);

        ingredientCategory.setDeletedAt(Instant.now());
        ingredientCategory.setDeletedBy(getAuthenticatedUser());

    }

    @Override
    @Transactional
    public void deleteIngredientsItems(Long id) throws Exception {

        IngredientsItems ingredientsItems = ingredientsItemsRepository.findById(id).orElseThrow();

        ingredientsItems.setDeletedAt(Instant.now());
        ingredientsItems.setDeletedBy(getAuthenticatedUser());

    }
}
