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

import me.amlu.model.Restaurant;
import me.amlu.model.User;
import me.amlu.request.CreateRestaurantRequest;
import me.amlu.response.MessageResponse;
import me.amlu.service.RestaurantService;
import me.amlu.service.Exceptions.UserNotFoundException;
import me.amlu.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin/restaurants")
public class AdminRestaurantController {

    private final RestaurantService restaurantService;

    private final UserService userService;

    public AdminRestaurantController(RestaurantService restaurantService, UserService userService) {
        this.restaurantService = restaurantService;
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<Restaurant> createRestaurant(
            @Valid @RequestBody CreateRestaurantRequest restaurantRequest,
            @RequestHeader("Authorization") String token)
            throws UserNotFoundException {
        User user = userService.findUserByJwtToken(token);

        Restaurant restaurant = restaurantService.createRestaurant(restaurantRequest, user);
        if (restaurant == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
        return new ResponseEntity<>(restaurant, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(
            @Valid @RequestBody CreateRestaurantRequest restaurantRequest,
            @RequestHeader("Authorization") String token,
            @PathVariable Long id)
            throws Exception {
        User user = userService.findUserByJwtToken(token);

        Restaurant restaurant = restaurantService.updateRestaurant(id, restaurantRequest);
        if (restaurant == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteRestaurant(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id)
            throws Exception {
        User user = userService.findUserByJwtToken(token);

        restaurantService.deleteRestaurant(id);

        MessageResponse response = new MessageResponse();
        response.setMessage("Restaurant deleted successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Restaurant> updateRestaurantStatus(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id)
            throws Exception {
        User user = userService.findUserByJwtToken(token);

        Restaurant restaurant = restaurantService.updateRestaurantStatus(id);

        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<Optional<Restaurant>> findRestaurantByUserId(
            @RequestHeader("Authorization") String token)
            throws Exception {
        User user = userService.findUserByJwtToken(token);

        Optional<Restaurant> restaurants = restaurantService.getRestaurantsByUserId(user.getId());

        return ResponseEntity.ok(restaurants);
    }
}
