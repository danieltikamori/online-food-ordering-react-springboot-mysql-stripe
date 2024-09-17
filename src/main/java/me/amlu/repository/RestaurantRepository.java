/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

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

    @Query("SELECT r.id AS id, r.name AS name, r.cuisineType AS cuisineType, AVG(f.rating) AS averageRating, r.images[0] AS imageUrl FROM Restaurant r LEFT JOIN r.foods f GROUP BY r.id")
    List<RestaurantView> findAllRestaurants();
    interface RestaurantView { // Projection interface defined within the repository
        Long getId();
        String getName();
        String getCuisineType();
        Double getAverageRating();
        String getImageUrl();
    }
}
