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

    public Optional<Restaurant> getRestaurantsByUserId(Long userId) throws Exception;

    public List<Restaurant> getRestaurantsByCategory(String category) throws Exception;

    public List<Restaurant> getRestaurantsByCuisineType(String cuisineType) throws Exception;

    public List<Restaurant> getRestaurantsByAddress(String address) throws Exception;

    public List<Restaurant> getRestaurantsByOpeningHours(String openingHours) throws Exception;

    // Favorite methods

    public RestaurantDto addToFavorites(Long restaurantId, User user) throws Exception;

//    public FavoriteRestaurantDto removeFromFavorites(Long restaurantId, User user) throws Exception;


}
