/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.repository;

import me.amlu.model.OrderStatus;
import me.amlu.model.Order;
import me.amlu.model.User;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends BaseRepository<Order, Long> {

    List<Order> findByCustomerId(Long customerId);

    List<Order> findByRestaurantIdAndAndOrderStatus(Long restaurantId, OrderStatus status);

    @NonNull
    Optional<Order> findById(@NonNull Long orderId);

    void deleteAllByDeletedAtBefore(Instant threshold);

    List<Order> findByCustomer(User customer);

    Optional<Order> findByIdempotencyKey(String idempotencyKey);
}
