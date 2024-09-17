/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.service.listeners;

import me.amlu.events.CartItemQuantityUpdatedEvent;
import me.amlu.model.CartItem;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CartItemQuantityUpdatedEventListener {
    @EventListener
    public void handleCartItemQuantityUpdated(CartItemQuantityUpdatedEvent event) {

        // Extract the cart item from the event
        CartItem cartItem = event.cartItem();

        // TODO: Logic to handle the event (e.g., update recommendations, send notifications, update Store inventory)

    }
}