/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.dto;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class RestaurantListDto { // Specific DTO for listing restaurants
    private Long id;
    private String name;
    private String cuisineType;
    private Double averageRating;
    private String imageUrl; // Single image for thumbnail
}