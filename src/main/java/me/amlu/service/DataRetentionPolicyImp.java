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
import me.amlu.repository.OrderRepository;
import me.amlu.repository.RestaurantRepository;
import me.amlu.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

// Implementing a data retention policy
public class DataRetentionPolicyImp implements DataRetentionPolicy {

    private final AnonymizationScheduler anonymizationScheduler;
    private final DataDeletionScheduler dataDeletionScheduler;

    public DataRetentionPolicyImp(UserRepository userRepository,
                                  OrderRepository orderRepository,
                                  RestaurantRepository restaurantRepository,
                                  int retentionDaysBeforeAnonymization,
                                  int retentionDaysBeforeDatabaseRemotion,
                                  AnonymizationScheduler anonymizationScheduler,
                                  DataDeletionScheduler dataDeletionScheduler) {

        this.anonymizationScheduler = anonymizationScheduler;
        this.dataDeletionScheduler = dataDeletionScheduler;
    }

    @Override
    public int getRetentionDaysBeforeDatabaseRemotion() throws NoSuchMethodException {
        Method method = getClass().getMethod("applyRetentionPolicy", AnonymizedData.class);
        DataRetentionPolicyDays annotation = method.getAnnotation(DataRetentionPolicyDays.class);
        if (annotation != null) {
            return annotation.retentionDaysBeforeDatabaseRemotion();
        } else {
            throw new NoSuchMethodException("DataRetentionPolicyDays annotation not found on applyRetentionPolicy method.");
        }
    }

    @Override
    @DataRetentionPolicyDays(retentionDaysBeforeAnonymization = 91, retentionDaysBeforeDatabaseRemotion = 181)
    public void applyRetentionPolicy(AnonymizedData data) throws Exception {
        // Schedule anonymization and deletion tasks
        anonymizationScheduler.anonymizeData();
        dataDeletionScheduler.deleteDataAfterRetentionPeriod();
    }

    @Override
    @Transactional
    public void deleteDataAfterRetentionPeriod(AnonymizedData data, int retentionDays) throws Exception {
        // Implement data deletion logic based on data type
        data.delete();
    }
}
