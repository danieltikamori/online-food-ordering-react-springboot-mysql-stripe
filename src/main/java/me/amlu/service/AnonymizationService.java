package me.amlu.service;

import me.amlu.model.Restaurant;
import me.amlu.model.User;

public interface AnonymizationService {
    void anonymizeUser(User user);

    void anonymizeRestaurant(Restaurant restaurant);

    boolean isUserAnonymized(User user);

    boolean isRestaurantAnonymized(Restaurant restaurant);
}
