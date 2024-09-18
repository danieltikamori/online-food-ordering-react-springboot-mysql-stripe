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

import me.amlu.dto.FoodDto;
import me.amlu.model.Category;
import me.amlu.model.Food;
import me.amlu.model.Restaurant;
import me.amlu.request.CreateFoodRequest;

import java.util.List;

public interface FoodService {

    public Food createFood(CreateFoodRequest createFoodRequest, Category category, Restaurant restaurant) throws Exception;

    void deleteFood(Long foodId, Restaurant restaurant, Long userId) throws Exception;

    public List<Food> getRestaurantsFood(Long restaurantId,
                                         boolean isVegetarian,
                                         boolean isNonVegetarian,
                                         boolean isSeasonal,
                                         String foodCategory) throws Exception;

    public List<Food> searchFood(String keyword) throws Exception;
    public Food findFoodById(Long foodId) throws Exception;
//    public List<Food> findFoodByCategory(Category category) throws Exception;
//    public List<Food> getAllFoods();

    public Food updateAvailabilityStatus(Long foodId) throws Exception;

    FoodDto getFoodIngredients(Long restaurantId, Long foodId) throws Exception;
}
