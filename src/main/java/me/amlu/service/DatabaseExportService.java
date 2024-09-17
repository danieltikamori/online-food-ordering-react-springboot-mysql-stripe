/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.service;

import lombok.NonNull;
import me.amlu.model.Order;
import me.amlu.model.Restaurant;
import me.amlu.model.User;

public interface DatabaseExportService {
    void exportAnonymizedUserData(@NonNull User user) throws Exception;
    void exportAnonymizedRestaurantData(Restaurant restaurant) throws Exception;
    void exportOrderData(Order order) throws Exception;
}
