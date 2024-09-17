/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.service;

import lombok.NonNull;
import me.amlu.dto.CartDto;
import me.amlu.dto.CartToOrderDto;
import me.amlu.model.Cart;
import me.amlu.model.CartItem;
import me.amlu.request.AddCartItemRequest;
import me.amlu.service.exceptions.CustomerNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CartService {

    CartItem addItemToCart(AddCartItemRequest request, String token) throws Exception;

    CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws Exception;

    Cart removeCartItem(Long cartItemId, String token) throws Exception;

//    BigDecimal calculateTotalAmount(Cart cart) throws Exception;

    @Transactional
    @CacheEvict(value = "carts", key = "#customerId")
    void clearCart(@NonNull Long customerId) throws CustomerNotFoundException;

    Cart findCartById(Long cartId) throws Exception;

    Optional<CartDto> findCartByCustomerId(Long customerId) throws Exception;

    Optional<CartToOrderDto> findCartWithItemsAndFoodDetails(Long customerId) throws Exception;


//    Optional<Cart> clearCart(Long customerId) throws Exception;



}
