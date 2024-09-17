/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.controller;

import me.amlu.model.Restaurant;
import me.amlu.model.User;
import me.amlu.request.CreateRestaurantRequest;
import me.amlu.response.MessageResponse;
import me.amlu.service.RestaurantService;
import me.amlu.service.exceptions.UserNotFoundException;
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

    @PutMapping("/{category_id}")
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

    @DeleteMapping("/{category_id}")
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

    @PutMapping("/{category_id}/status")
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

        Optional<Restaurant> restaurants = restaurantService.getRestaurantsByUserId(user.getUser_id());

        return ResponseEntity.ok(restaurants);
    }
}
