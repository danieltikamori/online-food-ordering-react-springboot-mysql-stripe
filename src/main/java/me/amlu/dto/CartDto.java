/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.dto; // Put your DTOs in a dedicated package

import lombok.AllArgsConstructor;
import lombok.Data;
import me.amlu.model.CartItem;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class CartDto {
    private Long cart_id;
    private List<CartItem> cartItems;
    private BigDecimal totalAmount; 
}