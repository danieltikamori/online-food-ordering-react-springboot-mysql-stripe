/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
 */

package me.amlu.controller;

import me.amlu.model.Cart;
import me.amlu.model.CartItem;
import me.amlu.model.User;
import me.amlu.request.AddCartItemRequest;
import me.amlu.request.UpdateCartItemRequest;
import me.amlu.service.CartService;
import me.amlu.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class CartController {

    private final CartService cartService;

    private final UserService userService;

    private final Logger log = LoggerFactory.getLogger(CartController.class);


    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }


    @PutMapping("/cart/add")
    public ResponseEntity<CartItem> addItemToCart(@Valid @RequestBody AddCartItemRequest request,
                                           @RequestHeader("Authorization") String token) throws Exception {
        // 1. Extract JWT token from Authorization header
//        String token = authHeader.substring("Bearer ".length());

        // 2. Validate JWT token and add item to cart
//        log.info("Received token: {}", token);
        CartItem cartItem = cartService.addItemToCart(request, token);

        // 3. Return the cart item
        return ResponseEntity.status(HttpStatus.OK).body(cartItem);


//        return new ResponseEntity<>(cartItem, HttpStatus.OK);
    }

    @PutMapping("/cart-item/update")
    public ResponseEntity<CartItem> updateCartItemQuantity(@Valid @RequestBody UpdateCartItemRequest request,
                                                           @RequestHeader("Authorization") String token) throws Exception {
        CartItem cartItem = cartService.updateCartItemQuantity(request.getCartItemId(), request.getQuantity());

        return ResponseEntity.status(HttpStatus.OK).body(cartItem);
//        return new ResponseEntity<>(cartItem, HttpStatus.OK);
    }

    @DeleteMapping("/cart-item/{id}/remove")
    public ResponseEntity<Cart> removeCartItem(@PathVariable Long id,
                                                 @RequestHeader("Authorization") String token) throws Exception {
        Cart cartItem = cartService.removeCartItem(id, token);
        return ResponseEntity.status(HttpStatus.OK).body(cartItem);
    }

    @PutMapping("/cart/clear")
    public ResponseEntity<Cart> clearCart(@RequestHeader("Authorization") String token) throws Exception {

        User user = userService.findUserByJwtToken(token);
        Cart clearCart = cartService.clearCart(user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(clearCart);
    }

    @GetMapping("/cart")
    public ResponseEntity<Cart> getCart(@RequestHeader("Authorization") String token) throws Exception {

        User user = userService.findUserByJwtToken(token);
        Cart cart = cartService.findCartByCustomerId(user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(cart);
    }

}
