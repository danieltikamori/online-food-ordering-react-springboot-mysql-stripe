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

import lombok.NonNull;
import me.amlu.model.Cart;
import me.amlu.model.CartItem;
import me.amlu.model.Food;
import me.amlu.model.User;
import me.amlu.repository.CartItemRepository;
import me.amlu.repository.CartRepository;
import me.amlu.request.AddCartItemRequest;
import me.amlu.service.Exceptions.CartItemNotFoundException;
import me.amlu.service.Exceptions.CartNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Service
public class CartServiceImp implements CartService {

    private final CartRepository cartRepository;

    private final UserService userService;

    private final CartItemRepository cartItemRepository;

    private final FoodService foodService;

    public CartServiceImp(CartRepository cartRepository, UserService userService, CartItemRepository cartItemRepository, FoodService foodService) {
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.cartItemRepository = cartItemRepository;
        this.foodService = foodService;
    }


    @Override
    public CartItem addItemToCart(AddCartItemRequest request, String token) throws Exception {

        User user = userService.findUserByJwtToken(token);

        Food food = foodService.findFoodById(request.getFoodId());

        Cart cart = cartRepository.findCartByCustomerId(user.getId());

        for (CartItem cartItem : cart.getCartItems()) {
            if (cartItem.getFood().equals(food)) {
                int newQuantity = cartItem.getQuantity() + request.getQuantity();
                return updateCartItemQuantity(cartItem.getId(), cartItem.getQuantity());
            }
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setCart(cart);
        newCartItem.setFood(food);
        newCartItem.setQuantity(request.getQuantity());
        newCartItem.setIngredients(request.getIngredients());
        newCartItem.setTotalAmount(food.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
        newCartItem.setUpdatedAt(Instant.now());
        newCartItem.setUpdatedBy((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        CartItem savedCartItem = cartItemRepository.save(newCartItem);
        cart.getCartItems().add(savedCartItem);

        return savedCartItem;
    }

    @Override
    public CartItem updateCartItemQuantity(@NonNull Long cartItemId, int quantity) throws Exception {
        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemId);
        if (cartItemOptional.isEmpty()) {
            throw new CartItemNotFoundException("Cart item not found.");
        }
        CartItem cartItem = cartItemOptional.get();
        cartItem.setQuantity(quantity);
        cartItem.setTotalAmount(cartItem.getFood().getPrice().multiply(BigDecimal.valueOf(quantity)));
        cartItem.setUpdatedAt(Instant.now());
        cartItem.setUpdatedBy((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        return cartItemRepository.save(cartItem);
    }

    @Override
    public Cart removeCartItem(@NonNull Long cartItemId, String token) throws Exception {

        User user = userService.findUserByJwtToken(token);

        Cart cart = cartRepository.findCartByCustomerId(user.getId());

        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemId);
        if (cartItemOptional.isEmpty()) {
            throw new CartItemNotFoundException("Cart item not found.");
        }
        CartItem cartItem = cartItemOptional.get();
        cart.getCartItems().remove(cartItem);
        cartItem.setUpdatedAt(Instant.now());
        cartItem.setUpdatedBy((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return cartRepository.save(cart);
    }

    @Override
    public BigDecimal calculateTotalAmount(Cart cart) throws Exception {

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem cartItem : cart.getCartItems()) {
            totalAmount = totalAmount.add(cartItem.getTotalAmount());
        }
        return totalAmount;

    }

    @Override
    public Cart findCartById(Long cartId) throws Exception {

        Optional<Cart> cartOptional = cartRepository.findById(cartId);
        if (cartOptional.isEmpty()) {
            throw new CartNotFoundException("Cart not found with id: " + cartId);
        }
        return cartOptional.get();
    }

    @Override
    public Cart findCartByCustomerId(Long customerId) throws Exception {

//        User user = userService.findUserByJwtToken(token);
        Cart cart = cartRepository.findCartByCustomerId(customerId);
        cart.setTotalAmount(calculateTotalAmount(cart));
        return cart;
    }

    @Override
    public Cart clearCart(@NonNull Long customerId) throws Exception {

//        User user = userService.findUserByJwtToken(token);
         Cart cart = findCartByCustomerId(customerId);

        cart.getCartItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
        cart.setUpdatedAt(Instant.now());
        cart.setUpdatedBy((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        return cartRepository.save(cart);
    }
}
