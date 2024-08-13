package me.amlu.repository;

import me.amlu.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerId(Long customerId);

    List<Order> findByRestaurantId(Long restaurantId);

    @NonNull
    Optional<Order> findById(@NonNull Long orderId);

    void deleteAllByDeletedAtBefore(Instant threshold);
}
