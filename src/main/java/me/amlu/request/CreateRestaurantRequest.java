/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import me.amlu.model.Address;
import me.amlu.model.ContactInformation;

import java.util.List;

@Data
public class CreateRestaurantRequest {

    @Positive(message = "Id must be a positive number.")
    private Long id;

    @Size(max = 127)
    @NotEmpty(message = "Name cannot be blank.")
    private String restaurantName;

    @Size(max = 2047)
    private String description;

    @Size(max = 255)
    @NotEmpty(message = "Cuisine type cannot be blank.")
    private String cuisineType;

    @NotEmpty
    private Address address;

    @NotEmpty
    private ContactInformation contactInformation;

    @NotEmpty(message = "Opening hours cannot be blank.")
    private String openingHours;

    @Size(max = 8191)
    @NotEmpty(message = "Images cannot be empty.")
    private List<String> images;

}
