/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.config;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import me.amlu.common.PostalCodeUtils;
import me.amlu.dto.AddressDto;

public class ValidPostalCodeValidator implements ConstraintValidator<ValidPostalCode, String> {

    private AddressDto addressDto;

    @Override
    public boolean isValid(String postalCode, ConstraintValidatorContext context) {
        if (postalCode == null) {
            return true; // Let @NotBlank handle null values
        }
        // Get the country code from your AddressDto (you might need to pass it as a parameter)
        String countryCode = addressDto.getCountry(); // Replace with logic to get country code from DTO
        String regex = PostalCodeUtils.getPostalCodeRegex(countryCode);
        return postalCode.matches(regex);
    }
}
