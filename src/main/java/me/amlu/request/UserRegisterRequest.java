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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import me.amlu.config.StrongPassword;
import me.amlu.dto.RestaurantDto;
import me.amlu.model.Address;
import me.amlu.model.Order;
import me.amlu.model.USER_ROLE;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserRegisterRequest {

    @Column(nullable = false, length = 255)
    @NotNull
    @NotBlank
    @Size(max = 255)
    private String fullName;

    @Column(nullable = false, unique = true, length = 255)
    @NotNull
    @NotBlank
    @Size(max = 255)
    private String email;

    private Instant deletedAt;

    // To avoid password leakage, we will use the JsonProperty annotation as write-only.
    @StrongPassword
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    @NotNull
    @NotBlank
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    @NotBlank
    private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "customer")
    @ToString.Exclude
    private List<Order> orders = new ArrayList<>();

    @ElementCollection
    private List<RestaurantDto> favoriteRestaurants = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Column(length = 8191)
    private List<Address> addresses = new ArrayList<>();
}
