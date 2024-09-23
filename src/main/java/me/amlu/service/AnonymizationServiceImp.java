/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
 */

package me.amlu.service;

import com.google.i18n.phonenumbers.Phonenumber;
import me.amlu.model.*;
import me.amlu.repository.AddressRepository;
import me.amlu.repository.RestaurantRepository;
import me.amlu.repository.UserRepository;
import me.amlu.service.tasks.RandomValueGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.CopyOnWriteArrayList;

import static me.amlu.common.SecurityUtil.getAuthenticatedUser;

@Service
public class AnonymizationServiceImp implements AnonymizationService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final NotificationService notificationService;


    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(AnonymizationServiceImp.class.getName());

    public static final Logger logger = LogManager.getLogger(AnonymizationServiceImp.class.getName());

    public AnonymizationServiceImp(UserRepository userRepository, RestaurantRepository restaurantRepository, NotificationService notificationService,
                                   AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.notificationService = notificationService;
    }

    @Override
    public void anonymizeUser(User user) {
        try {
            if (user.getDeletedAt() != null) {
                // Pseudonymize the user name
                user.setFullName(Instant.now().toString() + "-Anonymized User");

                // De-identify the user email with a random value
                String newEmail;
                do {
                    newEmail = RandomValueGenerator.generateRandomValue() + "@anonymizedUserEmail.com";
                } while (userRepository.existsByEmail(newEmail)); // Check if email already exists

                // Update the user's email with the new value
                user.setEmail(newEmail);

//            // Mask the user password
                user.setPassword("********");

                user.setAddresses(null);
                user.setFavoriteRestaurants(null);
                user.setCreatedBy(user);
                user.setAnonymizedAt(Instant.now());
                user.setAnonymizedBy(getAuthenticatedUser());

                // Save the anonymized user to the database
                userRepository.save(user);
                log.info(Instant.now().toString() + " - User anonymized successfully");
                logger.info("User anonymized successfully");
            }
            // Return true to indicate that the user was successfully anonymized
        } catch (Exception e) {
            // Log the exception and return false to indicate that the anonymization failed
            log.severe(Instant.now().toString() + " - Error anonymizing user: " + e.getMessage());
            logger.error("Error anonymizing user: {}", e.getMessage());
            notificationService.sendNotification(Instant.now().toString() + " - Error anonymizing user: ", e.getMessage());
        }
    }

    @Override
    public void anonymizeRestaurant(Restaurant restaurant) {


        try {
            Address address = restaurant.getAddress();
            address.setStreetAddress(Instant.now().toString());
            address.setCity(Instant.now().toString());
            address.setStateProvince(Instant.now().toString());
            address.setPostalCode(Instant.now().toString());
            address.setCountry(Instant.now().toString());

            User owner = restaurant.getOwner();
            owner.setFullName(Instant.now().toString() + "-Anonymized User");
            owner.setEmail(RandomValueGenerator.generateRandomValue() + "@anonymizedUserEmail.com");
            owner.setPassword("********");
            owner.setAddresses(null);
            owner.setFavoriteRestaurants(null);
            owner.setAnonymizedAt(Instant.now());
            owner.setAnonymizedBy(getAuthenticatedUser());

            ContactInformation contactInformation = restaurant.getContactInformation();
            contactInformation.setEmail(RandomValueGenerator.generateRandomValue() + "@anonymizedRestaurantEmail.com");
            Phonenumber.PhoneNumber phoneNumber = contactInformation.getPhoneNumber();
            phoneNumber.setCountryCode(0);
            phoneNumber.setNationalNumber(0);
            phoneNumber.setExtension("0");
            contactInformation.setPhoneNumber(phoneNumber);
            contactInformation.setMobile(null);
            contactInformation.setWebsite(null);
            contactInformation.setTwitter(null);
            contactInformation.setInstagram(null);

            CopyOnWriteArrayList<String> images = new CopyOnWriteArrayList<>();
            for (String image : restaurant.getImagesURL()) images.replaceAll(s -> "https://anonymized.com/img/");

            restaurant.setOwner(owner);
            restaurant.setRestaurantName(Instant.now().toString() + "-Anonymized Restaurant");
            restaurant.setCuisineType(null);
            restaurant.setAddress(address);
            restaurant.setDescription(null);
            restaurant.setContactInformation(contactInformation);
            restaurant.setOpeningHours("Anonymized Opening Hours");
            restaurant.setImagesURL(images);
            restaurant.setOpenNow(false);
            restaurant.setAnonymizedAt(Instant.now());
            restaurant.setAnonymizedBy(getAuthenticatedUser());


            // Save the anonymized restaurant to the database
            restaurantRepository.save(restaurant);
            log.info(Instant.now().toString() + " - Restaurant anonymized successfully");
            logger.info("Restaurant anonymized successfully");
            // Return true to indicate that the restaurant was successfully anonymized
        } catch (Exception e) {
            // Log the exception and return false to indicate that the anonymization failed
            log.severe(Instant.now().toString() + " - Error anonymizing restaurant: " + e.getMessage());
            notificationService.sendNotification(Instant.now().toString() + " - Error anonymizing restaurant: ", e.getMessage());
        }
    }

    @Override
    public boolean isUserAnonymized(User user) {
        return user.getAnonymizedAt() != null;
    }

    @Override
    public boolean isRestaurantAnonymized(Restaurant restaurant) {
        return restaurant.getAnonymizedAt() != null;
    }
}