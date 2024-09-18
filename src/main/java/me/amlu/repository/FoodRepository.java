/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
 */

package me.amlu.repository;

import me.amlu.model.Category;
import me.amlu.model.Food;
import me.amlu.model.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoodRepository extends BaseRepository<Food, Long> {

    List<Food> findByRestaurantId(Long restaurantId);

    @Query("SELECT f FROM Food f WHERE f.deletedAt IS NULL AND (LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR f.foodCategory.categoryName LIKE %:keyword%)")
//    @Query("SELECT f FROM Food f WHERE f.name LIKE %:keyword% OR f.foodCategory.categoryName LIKE %:keyword%")
    List<Food> searchFoodIgnoreCase(@Param("keyword") String keyword);

    @Query("SELECT f FROM Food f WHERE f.foodCategory.categoryName = :categoryName AND f.deletedAt IS NULL")
    List<Food> findByFoodCategory(@Param("categoryName") Category categoryName);

    boolean existsByNameAndFoodCategory(String name, Category foodCategory);

    @Query("SELECT f FROM Food f WHERE f.name = :name AND f.foodCategory = :foodCategory AND f.deletedAt IS NULL AND f.restaurant = :restaurant")
    boolean existsByNameAndFoodCategoryAndRestaurant(@Param(value = "name") String name, @Param(value = "foodCategory") Category foodCategory, @Param(value = "restaurant") Restaurant restaurant);

    boolean existsByNameAndFoodCategoryId(String name, Long foodCategoryId);

    boolean existsByNameAndRestaurantId(String name, Long restaurantId);

    boolean existsByNameAndFoodCategoryIdAndRestaurantId(String name, Long foodCategoryId, Long restaurantId);

//    boolean existsByFoodCategoryIdAndRestaurantId(Long foodCategoryId, Long restaurantId);
}
