/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.model;

import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.persistence.EntityListeners;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.amlu.config.SensitiveData;
import me.amlu.dto.RestaurantDto;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AnonymizedData {

    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    User user;
    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    private String name;
    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    private String email;
    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    private List<RestaurantDto> favoriteRestaurants;
    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    private List<Address> addresses;

    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    Restaurant restaurant;
    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    private String restaurantName;
    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    private String description;
    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    private String cuisineType;
    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    private String address;
    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    private String openingHours;
    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    private List<String> images;
    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    private boolean openNow;
    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    private List<Food> foods;
    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    private List<ContactInformation> contactInformation;

    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    private Phonenumber.PhoneNumber phoneNumber;
    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    private String mobile;
    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    private String website;
    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    private String twitter;
    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    private String instagram;

}