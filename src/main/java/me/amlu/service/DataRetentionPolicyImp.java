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
import me.amlu.repository.UserRepository;
import me.amlu.service.Tasks.RecordCleanupTask;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

// Implementing a data retention policy
public class DataRetentionPolicyImp implements DataRetentionPolicy {

    private int retentionDays;

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public DataRetentionPolicyImp(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    public int getRetentionDays() throws NoSuchMethodException {
        // You need to implement a way to retrieve the retention days value from the annotation
        // One way to do this is by using Java Reflection
        Method method = getClass().getMethod("applyRetentionPolicy", AnonymizedData.class);
        DataRetentionPolicyDays annotation = method.getAnnotation(DataRetentionPolicyDays.class);
        return annotation.getRetentionDays();
    }

    @DataRetentionPolicyDays(getRetentionDays = 90)
    @Override
    public void applyRetentionPolicy(AnonymizedData data, Annotation annotation) throws Exception {
//        // Determine the retention period based on the type of data and the purpose of the collection and processing
//        int retentionPeriod = determineRetentionPeriod(data);

        if (annotation instanceof DataRetentionPolicyDays) {
            int retentionDays = ((DataRetentionPolicyDays) annotation).getRetentionDays();
            // Use the retentionDays value to determine the retention period
           }

        // Delete the data after the retention period has expired
        deleteDataAfterRetentionPeriod(data, retentionDays);
    }

//    private int determineRetentionPeriod(AnonymizedData data) {
//        // Implement a mechanism to determine the retention period based on the type of data and the purpose of the collection and processing
//        // For example, use a lookup table or a rules engine
//        return 90; // Replace with the actual implementation
//    }

    @Override
    public void deleteDataAfterRetentionPeriod(AnonymizedData data, int retentionDays) throws Exception {
        // Implement a mechanism to delete the data after the retention period has expired
        // For example, use a scheduler or a timer

        DataRetentionPolicyImp dataRetentionPolicyImp = new DataRetentionPolicyImp(userRepository, orderRepository);

        RecordCleanupTask task = new RecordCleanupTask(userRepository, orderRepository, dataRetentionPolicyImp);
        task.deleteDeletedRecords();
    }

}