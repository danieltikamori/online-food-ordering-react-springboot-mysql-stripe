/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;
import me.amlu.config.StrongPassword;
import me.amlu.dto.RestaurantDto;
import me.amlu.model.Address;
import me.amlu.model.Order;
import me.amlu.model.UserRole;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserRegisterRequest {

    @NotEmpty
    @Size(max = 255, message = "Full name must be at most 255 characters long.")
    @Column(nullable = false, length = 255)
    private String fullName;

    @Email
    @NotEmpty
    @Size(max = 255, message = "Email must be at most 255 characters long.")
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    private Instant deletedAt;

    // To avoid password leakage, we will use the JsonProperty annotation as write-only.
    @StrongPassword
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    @NotEmpty
    @Size(min = 8, max = 255, message = "Password must be at least 8 characters long.")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotEmpty
    private UserRole role = UserRole.ROLE_CUSTOMER;

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
