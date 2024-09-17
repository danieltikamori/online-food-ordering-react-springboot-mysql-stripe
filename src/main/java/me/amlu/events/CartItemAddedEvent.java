/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.events;

import me.amlu.model.CartItem;

public record CartItemAddedEvent(CartItem cartItem) {

}