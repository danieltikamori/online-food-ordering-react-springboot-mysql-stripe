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

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
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