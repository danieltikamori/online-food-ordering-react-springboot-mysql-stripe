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

import jakarta.validation.Valid;
import lombok.NonNull;
import me.amlu.dto.CartDto;
import me.amlu.dto.CartToOrderDto;
import me.amlu.events.CartItemAddedEvent;
import me.amlu.events.CartItemQuantityUpdatedEvent;
import me.amlu.events.CartItemRemovedEvent;
import me.amlu.model.*;
import me.amlu.repository.CartItemRepository;
import me.amlu.repository.CartRepository;
import me.amlu.request.AddCartItemRequest;
import me.amlu.service.exceptions.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArraySet;

import static me.amlu.common.SecurityUtil.getAuthenticatedUser;

@Validated
@Service
public class CartServiceImp implements CartService {

    private static final Logger logger = LogManager.getLogger(CartServiceImp.class);
    private final UserService userService;
    private final FoodService foodService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CartServiceImp(UserService userService, FoodService foodService,
                          CartRepository cartRepository, CartItemRepository cartItemRepository, ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.foodService = foodService;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    @CacheEvict(value = "carts", key = "#user.category_id")
    public CartItem addItemToCart(@Valid AddCartItemRequest request, String token) throws UserNotFoundException, FoodNotFoundException {
        User user = userService.findUserByJwtToken(token);
        Food food = foodService.findFoodById(request.getFoodId());
        Cart cart = getOrCreateCart(user);

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getFood().getFood_id().equals(food.getFood_id()))
                .findFirst()
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setFood(food);
                    return newItem;
                });

        cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        cartItem.setIngredients((CopyOnWriteArraySet<IngredientsItems>) request.getIngredients());
        cartItem.setUpdatedAt(Instant.now());
        cartItem.setUpdatedBy(user);
        CartItem savedCartItem = cartItemRepository.save(cartItem);

        // Publish the event
        eventPublisher.publishEvent(new CartItemAddedEvent(savedCartItem));
        return savedCartItem;
    }

    @Override
    @Transactional
    @CacheEvict(value = "carts", key = "#cartItem.cart.category_id")
    public CartItem updateCartItemQuantity(@Valid @NonNull Long cartItemId, int quantity) throws CartItemNotFoundException {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found."));

        cartItem.setQuantity(quantity);
        cartItem.setUpdatedAt(Instant.now());
        cartItem.setUpdatedBy(getAuthenticatedUser());

// Publish the event
        eventPublisher.publishEvent(new CartItemQuantityUpdatedEvent(cartItem));

        return cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional
    @CacheEvict(value = "carts", key = "#cart.category_id")
    public Cart removeCartItem(@Valid @NonNull Long cartItemId, String token) throws UserNotFoundException, CartNotFoundException, CartItemNotFoundException {
        User user = userService.findUserByJwtToken(token);
        Optional<Cart> cartOptional = cartRepository.findCartByCustomerId(user.getUser_id());

        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            boolean removed = cart.getCartItems().removeIf(cartItem -> cartItem.getCart_item_id().equals(cartItemId));
            if (!removed) {
                throw new CartItemNotFoundException("Cart item not found.");
            }
            cartRepository.save(cart);

            // Publish the event
            CartItem cartItem = cartItemRepository.findById(cartItemId)
                    .orElseThrow(() -> new CartItemNotFoundException("Cart item not found."));
            eventPublisher.publishEvent(new CartItemRemovedEvent(cartItem));

            return cart;

        } else {
            throw new CartNotFoundException("Cart not found for user.");
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "carts", key = "#customerId")
    public void clearCart(@Valid @NonNull Long customerId) throws CustomerNotFoundException {
        cartItemRepository.deleteByCartCustomerIdAndCartDeletedAtIsNull(customerId);
    }

    @Override
    public Cart findCartById(Long cartId) throws CartNotFoundException {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with category_id: " + cartId));
    }

    @Override
    @Cacheable(value = "carts", key = "#customerId")
    public Optional<CartDto> findCartByCustomerId(Long customerId) throws CustomerNotFoundException {
        return cartRepository.findCartByCustomerId(customerId)
                .map(this::mapToCartDto); // Transform Cart to CartDTO
    }

    @Override
    public Optional<CartToOrderDto> findCartWithItemsAndFoodDetails(Long customerId) throws Exception {
        return cartRepository.findCartWithItemsAndFoodDetails(customerId);
    }

    // === Helper Methods ===

    private Cart getOrCreateCart(@Valid User user) {
        return cartRepository.findCartByCustomerId(user.getUser_id())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setCustomer(user);
                    return cartRepository.save(newCart);
                });
    }

    @Cacheable(value = "carts", key = "#cart.category_id")
    @CacheEvict(value = "carts", key = "#cart.category_id")
    public BigDecimal calculateCartTotal( @Valid Cart cart) {
        if (cart == null || cart.getCartItems() == null) {
            return BigDecimal.ZERO;
        }
        return cart.getCartItems().stream()
                .map(item -> item.getFood().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private CartDto mapToCartDto(@Valid Cart cart) {
        if (cart == null) {
            return null;
        }
        return new CartDto(
                cart.getCart_id(),
                cart.getCartItems(),
                calculateCartTotal(cart)
        );
    }

}