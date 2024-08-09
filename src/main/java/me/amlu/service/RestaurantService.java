package me.amlu.service;

import me.amlu.dto.RestaurantDto;
import me.amlu.model.Restaurant;
import me.amlu.model.User;
import me.amlu.request.CreateRestaurantRequest;

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

    public List<Restaurant> searchRestaurant(String keyword);

    public Restaurant getRestaurantById(Long id) throws Exception;

    public Optional<Restaurant> getRestaurantsByUserId(Long userId) throws Exception;

    public List<Restaurant> getRestaurantsByCategory(String category) throws Exception;

    public List<Restaurant> getRestaurantsByCuisineType(String cuisineType) throws Exception;

    public List<Restaurant> getRestaurantsByAddress(String address) throws Exception;

    public List<Restaurant> getRestaurantsByOpeningHours(String openingHours) throws Exception;

    // Favorite methods

    public RestaurantDto addToFavorites(Long restaurantId, User user) throws Exception;

//    public FavoriteRestaurantDto removeFromFavorites(Long restaurantId, User user) throws Exception;


}
