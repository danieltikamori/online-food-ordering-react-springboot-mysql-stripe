/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.service;

import me.amlu.model.Restaurant;
import me.amlu.model.User;

public interface DataTransferService {

    void transferUserData(User user) throws Exception;
    void transferRestaurantData(Restaurant restaurant) throws Exception; // Transfer restaurant Data
}