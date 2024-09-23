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

import me.amlu.model.Cart;
import me.amlu.model.CartItem;
import me.amlu.model.Food;
import me.amlu.model.User;
import me.amlu.repository.CartItemRepository;
import me.amlu.repository.CartRepository;
import me.amlu.request.AddCartItemRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
public class CartServiceImpTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserService userService;

    @Mock
    private FoodService foodService;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartServiceImp cartService;

    @Test
    public void testAddItemToCart_AddsToExistingCart() throws Exception {

        User user = new User();
        user.setUser_id(1L);

        Cart existingCart = new Cart();
        existingCart.setCart_id(5L);
        existingCart.setCustomer(user);

        AddCartItemRequest request = new AddCartItemRequest();
        request.setFoodId(100L);
        request.setQuantity(2);
        Food food = new Food();
        food.setFood_id(100L);
        food.setPrice(BigDecimal.valueOf(12.99));

        Mockito.when(userService.findUserByJwtToken(anyString())).thenReturn(user);
        Mockito.when(foodService.findFoodById(anyLong())).thenReturn(food);
        Mockito.when(cartRepository.findCartByCustomerId(user.getUser_id()))
                .thenReturn(Optional.of(existingCart));

        // Action
        CartItem savedCartItem = cartService.addItemToCart(request, "someToken");

        // Assertions
        assertNotNull(savedCartItem);
        assertNotNull(savedCartItem.getCart());
        assertEquals(5L, savedCartItem.getCart().getCart_id());
        Mockito.verify(cartRepository, Mockito.times(1)).save(existingCart);

        assertTrue(existingCart.getCartItems().contains(savedCartItem));
        Mockito.verify(cartItemRepository, Mockito.times(1)).save(any(CartItem.class));
    }

    @Test
    public void testAddItemToCart_CreatesNewCartWhenCartNotFound() throws Exception {

        User user = new User();
        user.setUser_id(1L);
        AddCartItemRequest request = new AddCartItemRequest();
        request.setFoodId(100L); // Example food ID
        request.setQuantity(2); // Example quantity
        Food food = new Food();
        food.setFood_id(100L);
        food.setPrice(BigDecimal.valueOf(12.99)); // Example price

        Mockito.when(userService.findUserByJwtToken(anyString())).thenReturn(user);
        Mockito.when(foodService.findFoodById(anyLong())).thenReturn(food);
        Mockito.when(cartRepository.findCartByCustomerId(user.getUser_id()))
                .thenReturn(Optional.empty()); // No existing cart
        Mockito.when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> {
            Cart cart = invocation.getArgument(0);
            cart.setCart_id(10L); // Set an ID for the saved cart
            return cart;
        });

        // Action
        CartItem savedCartItem = cartService.addItemToCart(request, "someToken");

        // Assertions
        assertNotNull(savedCartItem);
        assertNotNull(savedCartItem.getCart());
        assertEquals(10L, savedCartItem.getCart().getCart_id()); // New cart created
        assertEquals(1, savedCartItem.getCart().getCartItems().size()); // Item added to cart
    }

}