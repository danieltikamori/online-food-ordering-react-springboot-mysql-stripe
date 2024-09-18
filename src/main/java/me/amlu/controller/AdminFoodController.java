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

import me.amlu.model.Food;
import me.amlu.model.Restaurant;
import me.amlu.model.User;
import me.amlu.request.CreateFoodRequest;
import me.amlu.response.MessageResponse;
import me.amlu.service.FoodService;
import me.amlu.service.RestaurantService;
import me.amlu.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/food")
public class AdminFoodController {

    private final FoodService foodService;

    private final UserService userService;

    private final RestaurantService restaurantService;

    public AdminFoodController(FoodService foodService, UserService userService, RestaurantService restaurantService) {
        this.foodService = foodService;
        this.userService = userService;
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public ResponseEntity<Food> createFood(@Valid @RequestBody CreateFoodRequest createFoodRequest,
                                           @RequestHeader("Authorization") String token) throws Exception {
        User user = userService.findUserByJwtToken(token);
        Restaurant restaurant = restaurantService.findRestaurantById(createFoodRequest.getRestaurantId());
        Food food = foodService.createFood(createFoodRequest, createFoodRequest.getCategory(), restaurant);

        return new ResponseEntity<>(food, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteFood(@PathVariable Long id,
                                                      @RequestHeader("Authorization") String token) throws Exception {
        User user = userService.findUserByJwtToken(token);
        foodService.deleteFood(id);

        MessageResponse response = new MessageResponse();
        response.setMessage("Food deleted successfully.");

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Food> updateFoodAvailabilityStatus(@PathVariable Long id,
                                                      @RequestHeader("Authorization") String token) throws Exception {
        User user = userService.findUserByJwtToken(token);

        Food food = foodService.updateAvailabilityStatus(id);
        MessageResponse response = new MessageResponse();
        response.setMessage("Food updated successfully.");

        return new ResponseEntity<>(food, HttpStatus.OK);
    }

}
