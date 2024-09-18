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

import me.amlu.model.AnonymizedData;
import me.amlu.model.Restaurant;
import me.amlu.model.User;
import me.amlu.repository.RestaurantRepository;
import me.amlu.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class AnonymizationScheduler {

    private final AnonymizationService anonymizationService;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final NotificationService notificationService;


    public AnonymizationScheduler(AnonymizationService anonymizationService, UserRepository userRepository, RestaurantRepository restaurantRepository, NotificationService notificationService) {
        this.anonymizationService = anonymizationService;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.notificationService = notificationService;
    }

        @Scheduled(cron = "0 0 0 * * *") // Runs daily at midnight
    public void scheduleAnonymization(AnonymizedData data, int retentionDaysBeforeAnonymization) {

            Instant anonymizationThreshold = Instant.now().minus(retentionDaysBeforeAnonymization, ChronoUnit.DAYS);

            // Find all users deleted before the threshold
            List<User> usersToAnonymize = userRepository.findByDeletedAtBefore(anonymizationThreshold);
            for (User user : usersToAnonymize) {
                anonymizationService.anonymizeUser(user);
            }

            // Find all restaurants deleted before the threshold
            List<Restaurant> restaurantsToAnonymize = restaurantRepository.findByDeletedAtBefore(anonymizationThreshold);
            for (Restaurant restaurant : restaurantsToAnonymize) {
                anonymizationService.anonymizeRestaurant(restaurant);
            }

            notificationService.sendNotification( "Notification: Data was anonymized", Instant.now().toString() + "Data was anonymized");
    }
}
