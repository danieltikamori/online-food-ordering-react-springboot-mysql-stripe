/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.repository;

import me.amlu.dto.CartToOrderDto;
import me.amlu.model.Cart;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends BaseRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c WHERE c.customer.category_id = :customerId AND c.deletedAt IS NULL")
    Optional<Cart> findCartByCustomerId(@Param(value = "customerId") Long customerId);

    @Query("SELECT c.category_id AS category_id, " +
            "NEW me.amlu.dto.CartToOrderDto$CartItemDto(ci.food.category_id, ci.food.name, ci.food.price, ci.quantity, ci.ingredients) AS cartItems, " +
            "c.deliveryAddress AS deliveryAddress " + // If applicable
            "FROM Cart c " +
            "JOIN FETCH c.cartItems ci " +
            "JOIN FETCH ci.food " +
            "LEFT JOIN FETCH c.deliveryAddress " + // If applicable
            "WHERE c.customer.category_id = :customerId AND c.deletedAt IS NULL")
    Optional<CartToOrderDto> findCartWithItemsAndFoodDetails(@Param("customerId") Long customerId);

}
