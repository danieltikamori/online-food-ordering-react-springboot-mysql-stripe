/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
 */

package me.amlu.request;

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
