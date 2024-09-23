/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
 */

package me.amlu.service.tasks;

import me.amlu.repository.OrderRepository;
import me.amlu.repository.UserRepository;
import me.amlu.service.DataRetentionPolicyImp;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class RecordCleanupTask {

    private final UserRepository userRepository;

    private final OrderRepository orderRepository;

    private final DataRetentionPolicyImp dataRetentionPolicy;

    public RecordCleanupTask(UserRepository userRepository, OrderRepository orderRepository, DataRetentionPolicyImp dataRetentionPolicy) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.dataRetentionPolicy = dataRetentionPolicy;

    }


    @Scheduled(cron = "0 0 0 * * *") // run daily at midnight
    public void deleteDeletedRecords() throws NoSuchMethodException {
        if (dataRetentionPolicy == null || dataRetentionPolicy.getRetentionDaysBeforeDatabaseRemotion() == 0) {
            // Do nothing if no retention period is configured
            return;
        }

        Instant threshold = Instant.now().minus(dataRetentionPolicy.getRetentionDaysBeforeDatabaseRemotion(), ChronoUnit.DAYS);
        userRepository.deleteAllByDeletedAtBefore(threshold);
        orderRepository.deleteAllByDeletedAtBefore(threshold);
    }

}