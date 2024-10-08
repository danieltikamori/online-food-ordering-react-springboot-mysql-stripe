/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
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


//    TO TEST, CHECK
    @Query("SELECT r.id AS id, r.name AS name, r.cuisineType AS cuisineType, i AS imageUrl " +
            "FROM Restaurant r LEFT JOIN r.foods f LEFT JOIN r.imagesURL i " + // JOIN FETCH for images
            "WHERE r.deletedAt IS NULL " + // Soft delete handling
            "GROUP BY r.id")
    List<RestaurantView> findAllRestaurants();
    
    void getDeletedAt(Long restaurantId);

    void delete(Long restaurantId);

    void deleteAllByDeletedAtBefore(Instant deletionThreshold);

    Optional<Object> findByCategory(String category);

    List<Restaurant> findByCuisineType(String cuisineType);

    List<Restaurant> findByAddress_City(String city);

    List<Restaurant> findByOpeningHours(String openingHours);

    interface RestaurantView { // Projection interface defined within the repository
        Long getId();

        String getName();

        String getCuisineType();

        Double getAverageRating();

        String getImageUrl();
    }
}
