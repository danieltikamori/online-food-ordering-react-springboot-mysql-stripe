/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.dto;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import me.amlu.config.ValidPostalCode;

@Data
@Embeddable
public class AddressDto { @NotBlank(message = "Street address is required") @Size(max = 255, message = "Street address cannot exceed 255 characters") private String streetAddress;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City cannot exceed 100 characters")
    private String city;

    @NotBlank(message = "State/Province is required")
    @Size(max = 100, message = "State/Province cannot exceed 100 characters")
    private String stateProvince;

    @ValidPostalCode(message = "Invalid postal code format.")
    @NotBlank(message = "Postal/Zip code is required")
//    @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "Invalid US zip code format") // Example: US zip code validation
    private String postalCode;

    @NotBlank(message = "Country is required")
    @Size(min = 2, max = 2, message = "Country code must be 2 characters") // Assuming 2-letter country codes
    private String country;

}