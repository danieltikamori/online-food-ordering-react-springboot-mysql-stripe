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

import me.amlu.model.Order;
import me.amlu.model.User;
import me.amlu.request.OrderRequest;
import me.amlu.service.OrderService;
import me.amlu.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private final OrderService orderService;

    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping("/order")
    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderRequest orderRequest,
            @RequestHeader("Authorization") String token) throws Exception {

        User user = userService.findUserByJwtToken(token);
        Order order = orderService.createOrder(orderRequest, user);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/order/user")
    public ResponseEntity<List<Order>> getOrderHistory(@Valid @RequestBody OrderRequest orderRequest,
                                                       @RequestHeader("Authorization") String token) throws Exception {

        User user = userService.findUserByJwtToken(token);
        List<Order> orders = orderService.getCustomerOrders(user.getUser_id());
        if (orders == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

}
