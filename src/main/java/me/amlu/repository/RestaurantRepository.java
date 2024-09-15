package me.amlu.repository;

import me.amlu.model.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends BaseRepository<Restaurant, Long> {


    @Query("SELECT r FROM Restaurant r WHERE r.deletedAt IS NULL AND (" +
            "LOWER(r.restaurantName) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(r.cuisineType) LIKE LOWER(CONCAT('%', :searchQuery, '%')))")
    List<Restaurant> findBySearchQueryIgnoreCase(@Param("searchQuery") String searchQuery);

    Optional<Restaurant> findByOwnerId(Long userId);  // Updated method signature


    boolean existsByOwnerId(Long userId);

    List<Restaurant> findByDeletedAtBefore(Instant anonymizationThreshold);
}
