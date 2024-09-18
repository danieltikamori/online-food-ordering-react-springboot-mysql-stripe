/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
 */

package me.amlu.controller;

import me.amlu.dto.FoodDto;
import me.amlu.model.Food;
import me.amlu.model.User;
import me.amlu.service.FoodService;
import me.amlu.service.RestaurantService;
import me.amlu.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/food")
public class FoodController {

    private final FoodService foodService;

    private final UserService userService;

    private final RestaurantService restaurantService;


    public FoodController(FoodService foodService, UserService userService, RestaurantService restaurantService) {
        this.foodService = foodService;
        this.userService = userService;
        this.restaurantService = restaurantService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Food>> searchFood(@RequestParam String name,
                                           @RequestHeader("Authorization") String token) throws Exception {

        User user = userService.findUserByJwtToken(token);

        List<Food> searchedFoods = foodService.searchFood(name);

        return new ResponseEntity<>(searchedFoods, HttpStatus.OK);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Food>> getRestaurantFood(@RequestParam boolean vegetarian,
                                                 @RequestParam (required = false) boolean nonVegetarian,
                                                 @RequestParam (required = false)boolean seasonal,
                                                 @RequestParam(required = false) String food_category,
                                                 @PathVariable Long restaurantId,
                                                 @RequestHeader("Authorization") String token) throws Exception {

        User user = userService.findUserByJwtToken(token);

        List<Food> foods = foodService.getRestaurantsFood(restaurantId, vegetarian, nonVegetarian, seasonal, food_category);
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }

    @GetMapping("/restaurant/{restaurantId}/{foodId}")
    public ResponseEntity<FoodDto> getFoodIngredients(@PathVariable Long restaurantId,
                                                      @PathVariable Long foodId,
                                                      @RequestHeader("Authorization") String token) throws Exception {

        // Validate the token and get the user details
        User user = userService.findUserByJwtToken(token);

        // If the user has the necessary permissions, proceed with the request
        FoodDto foodDto = foodService.getFoodIngredients(restaurantId, foodId);
        return new ResponseEntity<>(foodDto, HttpStatus.OK);
    }



}
