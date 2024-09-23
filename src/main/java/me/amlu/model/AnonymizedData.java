/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
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
public abstract class AnonymizedData {

    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    private Long id;
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

    public abstract void delete() throws Exception;
}