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

import lombok.extern.slf4j.Slf4j;
import me.amlu.repository.OrderRepository;
import me.amlu.repository.RestaurantRepository;
import me.amlu.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Component
public class DataDeletionScheduler {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;
    private final int retentionDaysBeforeDatabaseDeletion;

    public static final Logger logger = LogManager.getLogger(DataDeletionScheduler.class);
    private final NotificationService notificationService;

    public DataDeletionScheduler(UserRepository userRepository,
                                 RestaurantRepository restaurantRepository,
                                 OrderRepository orderRepository,
                                 @Value("${data.retention.deletion.days:180}") int retentionDaysBeforeDatabaseDeletion, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderRepository = orderRepository;
        this.retentionDaysBeforeDatabaseDeletion = retentionDaysBeforeDatabaseDeletion;
        this.notificationService = notificationService;
    }

    @Scheduled(cron = "0 0 0 * * *") // Runs daily at midnight
    @Transactional
    public void deleteDataAfterRetentionPeriod() {

        try {
            Instant deletionThreshold = Instant.now().minus(retentionDaysBeforeDatabaseDeletion, ChronoUnit.DAYS);

            userRepository.deleteAllByDeletedAtBefore(deletionThreshold);
            restaurantRepository.deleteAllByDeletedAtBefore(deletionThreshold);
            orderRepository.deleteAllByDeletedAtBefore(deletionThreshold);

            log.info("Data deletion completed for records deleted before {}", deletionThreshold);
        } catch (Exception e) {
            log.error("Error deleting data", e);
            logger.error("Error deleting data", e);
            notificationService.sendNotification("Error deleting data", e.getMessage());
            throw new RuntimeException("Error deleting data", e);
        }
    }
}