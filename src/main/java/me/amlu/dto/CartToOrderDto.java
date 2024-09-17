/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.dto;

import jakarta.persistence.Embeddable;
import lombok.Data;
import me.amlu.model.Address;

import java.util.List;

@Data
@Embeddable
public class CartToOrderDto {

    private Long id;
    private List<CartItemDto> cartItems;
    private Address deliveryAddress;

}