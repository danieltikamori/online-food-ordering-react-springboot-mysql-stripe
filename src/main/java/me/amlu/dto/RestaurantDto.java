/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
 */

package me.amlu.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import me.amlu.config.SensitiveData;
import me.amlu.model.Address;
import me.amlu.model.Restaurant;
import me.amlu.model.User;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Data
@Embeddable
public class RestaurantDto {

    @Positive
    private Long restaurant_id;

    @NotEmpty
    @Size(max = 255)
    private String restaurantName;

    @NotEmpty
    @Column(length = 8191)
    @Size(max = 8191)
    private List<String> imagesURL;

    @Column(length = 2047)
    @Size(max = 2047)
    private String description;

    @NotEmpty
    @Size(max = 127)
    private String cuisineType;

    @NotEmpty
    @ManyToOne
    private Address address;

    @NotEmpty
    @ManyToOne
    private User owner;

    @NotEmpty
    @ManyToOne
    private ContactInformationDto contactInformation;

    @NotEmpty
    @Size(max = 255)
    private String openingHours;

    @CreatedDate
    @NotEmpty
    @Column(nullable = false)
    private Instant createdAt;

    @CreatedBy
    @NotEmpty
    @ManyToOne
    private User createdBy;

    @LastModifiedDate
    @NotEmpty
    @Column(nullable = false)
    private Instant updatedAt;

    @LastModifiedBy
    @ManyToOne
    @JoinColumn(name = "updated_by_id")
    private User updatedBy;

    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    private Instant deletedAt;

    @ManyToOne
    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    @JoinColumn(name = "deleted_by_id")
    private User deletedBy;

    // DTO for Minimal Restaurant Information
    public static RestaurantDto fromEntity(Restaurant restaurant) {
        // ... map only non-sensitive fields
        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setRestaurant_id(restaurant.getRestaurant_id());
        restaurantDto.setRestaurantName(restaurant.getRestaurantName());
        restaurantDto.setDescription(restaurant.getDescription());
        restaurantDto.setImagesURL(restaurant.getImagesURL());
        restaurantDto.setCuisineType(restaurant.getCuisineType());
        restaurantDto.setAddress(restaurant.getAddress());
        restaurantDto.setContactInformation(ContactInformationDto.fromEntity(restaurant.getContactInformation()));
        restaurantDto.setOpeningHours(restaurant.getOpeningHours());
        return restaurantDto;
    }


    /**
     * This method is used to compare the equality of two objects.
     * It is overridden from the Object class to provide a custom implementation
     * for the RestaurantDto class.
     *
     * @param o the object to be compared with the current object
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        // First, we check if the object being compared is the same as the current object
        // If they are the same, we can immediately return true
        if (this == o) return true;

        // Next, we check if the object being compared is null or if it is not an instance of RestaurantDto
        // If either of these conditions is true, we can immediately return false
        if (o == null || getClass() != o.getClass()) return false;

        // Now, we can safely cast the object being compared to a RestaurantDto
        RestaurantDto that = (RestaurantDto) o;

        // Finally, we compare the category_id fields of the two objects
        // If they are equal, we return true; otherwise, we return false
        return Objects.equals(restaurant_id, that.restaurant_id);
    }

    /**
     * This method is used to generate a hash code for the object.
     * It is overridden from the Object class to provide a custom implementation
     * for the RestaurantDto class.
     *
     * @return a hash code for the object
     */
    @Override
    public int hashCode() {
        // We use the Objects.hash method to generate a hash code based on the category_id field
        // This is because the category_id field is used in the equals method to determine equality
        // By using the same field to generate the hash code, we ensure that equal objects have equal hash codes
        return Objects.hash(restaurant_id);
    }

}
