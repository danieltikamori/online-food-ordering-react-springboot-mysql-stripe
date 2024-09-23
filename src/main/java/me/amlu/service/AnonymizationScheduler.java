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

import me.amlu.model.Restaurant;
import me.amlu.model.User;
import me.amlu.repository.RestaurantRepository;
import me.amlu.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class AnonymizationScheduler {

    private static final Logger log = LoggerFactory.getLogger(AnonymizationScheduler.class);

    private final AnonymizationService anonymizationService;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final NotificationService notificationService;
    private final int retentionDaysBeforeAnonymization;

    public AnonymizationScheduler(AnonymizationService anonymizationService,
                                  UserRepository userRepository,
                                  RestaurantRepository restaurantRepository,
                                  NotificationService notificationService,
                                  @Value("${data.retention.anonymization.days:90}") int retentionDaysBeforeAnonymization) {
        this.anonymizationService = anonymizationService;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.notificationService = notificationService;
        this.retentionDaysBeforeAnonymization = retentionDaysBeforeAnonymization;
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void anonymizeData() {
        Instant anonymizationThreshold = Instant.now().minus(retentionDaysBeforeAnonymization, ChronoUnit.DAYS);

        List<User> anonymizedUsers = userRepository.findByDeletedAtBefore(anonymizationThreshold);
        anonymizedUsers.forEach(anonymizationService::anonymizeUser);

        List<Restaurant> anonymizedRestaurants = restaurantRepository.findByDeletedAtBefore(anonymizationThreshold);
        anonymizedRestaurants.forEach(anonymizationService::anonymizeRestaurant);

        log.info("{} users and {} restaurants were anonymized.", anonymizedUsers.size(), anonymizedRestaurants.size());
        notificationService.sendNotification(
                "Data Anonymization Complete",
                String.format("Anonymized %d users and %d restaurants.", anonymizedUsers.size(), anonymizedRestaurants.size())
        );
    }
}
