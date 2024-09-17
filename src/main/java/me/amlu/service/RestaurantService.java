/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.service;

import me.amlu.dto.RestaurantDto;
import me.amlu.model.Restaurant;
import me.amlu.model.User;
import me.amlu.request.CreateRestaurantRequest;
import me.amlu.service.exceptions.RestaurantNotFoundException;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface RestaurantService {

    public Restaurant createRestaurant(CreateRestaurantRequest restaurantRequest, User user);

    public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updatedRestaurant) throws Exception;

    public Restaurant updateRestaurantStatus(Long restaurantId) throws Exception;

    public void deleteRestaurant(Long restaurantId) throws Exception;


    // List methods

    public List<Restaurant> getAllRestaurants();

    public Restaurant findRestaurantById(Long restaurantId) throws Exception;

//    public RestaurantDto getRestaurantById(Long category_id, Authentication authentication) throws RestaurantNotFoundException;


    public List<Restaurant> searchRestaurant(String keyword);

    public Optional<Restaurant> getRestaurantsByUserId(Long userId) throws Exception;

    public List<Restaurant> getRestaurantsByCategory(String category) throws Exception;

    public List<Restaurant> getRestaurantsByCuisineType(String cuisineType) throws Exception;

    public List<Restaurant> getRestaurantsByAddress(String address) throws Exception;

    public List<Restaurant> getRestaurantsByOpeningHours(String openingHours) throws Exception;

    // Favorite methods

    public RestaurantDto addToFavorites(Long restaurantId, User user) throws Exception;

//    public FavoriteRestaurantDto removeFromFavorites(Long restaurantId, User user) throws Exception;


}
