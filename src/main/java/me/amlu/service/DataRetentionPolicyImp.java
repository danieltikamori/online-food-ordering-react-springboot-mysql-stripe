/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.service;

import me.amlu.model.AnonymizedData;
import me.amlu.model.Restaurant;
import me.amlu.model.User;
import me.amlu.repository.OrderRepository;
import me.amlu.repository.RestaurantRepository;
import me.amlu.repository.UserRepository;
import me.amlu.service.Tasks.RecordCleanupTask;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

// Implementing a data retention policy
public class DataRetentionPolicyImp implements DataRetentionPolicy {

    private final int retentionDaysBeforeAnonymization;
    private final int retentionDaysBeforeDatabaseRemotion;
    private final AnonymizationScheduler anonymizationScheduler;
    private final DataDeletionScheduler dataDeletionScheduler;


//    public DataRetentionPolicyImp(int retentionDaysBeforeAnonymization,
//                                  int retentionDaysBeforeDatabaseRemotion,
//                                  AnonymizationScheduler anonymizationScheduler,
//                                  DataDeletionScheduler dataDeletionScheduler) {
//        this.retentionDaysBeforeAnonymization = retentionDaysBeforeAnonymization;
//        this.retentionDaysBeforeDatabaseRemotion = retentionDaysBeforeDatabaseRemotion;
//        this.anonymizationScheduler = anonymizationScheduler;
//        this.dataDeletionScheduler = dataDeletionScheduler;
//    }

    public DataRetentionPolicyImp(UserRepository userRepository, OrderRepository orderRepository, RestaurantRepository restaurantRepository, int retentionDaysBeforeAnonymization, int retentionDaysBeforeDatabaseRemotion, AnonymizationScheduler anonymizationScheduler, DataDeletionScheduler dataDeletionScheduler) {
        this.retentionDaysBeforeAnonymization = retentionDaysBeforeAnonymization;
        this.retentionDaysBeforeDatabaseRemotion = retentionDaysBeforeDatabaseRemotion;
        this.anonymizationScheduler = anonymizationScheduler;
        this.dataDeletionScheduler = dataDeletionScheduler;
    }

    public int getRetentionDaysBeforeDatabaseRemotion() throws NoSuchMethodException {
        // You need to implement a way to retrieve the retention days value from the annotation
        // One way to do this is by using Java Reflection
        Method method = getClass().getMethod("applyRetentionPolicy", AnonymizedData.class);
        DataRetentionPolicyDays annotation = method.getAnnotation(DataRetentionPolicyDays.class);
        return annotation.getRetentionDaysBeforeDatabaseRemotion();
    }



    @Override
    public void applyRetentionPolicy(AnonymizedData data) throws Exception {
//        // Determine the retention period based on the type of data and the purpose of the collection and processing
        int retentionDays = determineRetentionPeriod(data);

        anonymizationScheduler.scheduleAnonymization(data, retentionDaysBeforeAnonymization);
        transferData(data);
        dataDeletionScheduler.scheduleDeletion(data, retentionDays);
        }



//    private int determineRetentionPeriod(AnonymizedData data) {
//        // Implement a mechanism to determine the retention period based on the type of data and the purpose of the collection and processing
//        // For example, use a lookup table or a rules engine
//        return 90; // Replace with the actual implementation
//    }

    @Override
    @Transactional
    public void deleteDataAfterRetentionPeriod(AnonymizedData data, int retentionDays) throws Exception {
        // Implement a mechanism to delete the data after the retention period has expired
        // For example, use a scheduler or a timer

        DataRetentionPolicyImp dataRetentionPolicyImp = new DataRetentionPolicyImp(userRepository, restaurantRepository, orderRepository, anonymizationService);

        RecordCleanupTask task = new RecordCleanupTask(userRepository, orderRepository, dataRetentionPolicyImp);
        task.deleteDeletedRecords();
    }

}
