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

import lombok.NonNull;
import me.amlu.model.AnonymizedData;
import me.amlu.model.Restaurant;
import me.amlu.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;


@Service
public class AnonymizedDataTransferServiceImp implements AnonymizedDataTransferService {

    private final AnonymizationService anonymizationService;

    private final DataRetentionPolicy dataRetentionPolicy;

    private final NotificationService notificationService;

    //    private final DataTransferService dataTransferService;
    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(AnonymizedDataTransferServiceImp.class.getName());

    public static final Logger logger = LogManager.getLogger(AnonymizedDataTransferServiceImp.class.getName());

    public AnonymizedDataTransferServiceImp(AnonymizationService anonymizationService, NotificationService notificationService, DataRetentionPolicy dataRetentionPolicy) {
        this.anonymizationService = anonymizationService;
        this.dataRetentionPolicy = dataRetentionPolicy;
        this.notificationService = notificationService;
//        this.dataTransferService = dataTransferService;
    }

    @Override
    public void exportAnonymizedData(@NonNull User user) {

        // Export the data to a file
        try (FileOutputStream fos = new FileOutputStream("anonymized_data_" + user.getUser_id() + ".txt")) {
            fos.write(user.toString().getBytes());
        } catch (IOException e) {
// Handle the exception
            log.severe("Error exporting data: " + e.getMessage());
            // Send a notification to the development team
            notificationService.sendNotification("Error exporting data", e.getMessage());
        }
    }

    @Override
    public void transferAnonymizedUserData(User user) throws Exception {

        exportAnonymizedData(user);
        try {
            boolean isUserAnonymized = anonymizationService.isUserAnonymized(user);
            if (isUserAnonymized) {
//                TODO:
                //Transfer the data to a third-party service or storage
                dataRetentionPolicy.applyRetentionPolicy(user);
                log.info("User data was anonymized and transferred successfully.");
            } else {
                log.severe("User data was not anonymized.");

                // Send a notification to the development team
                notificationService.sendNotification("User data was not anonymized", "An error occurred during anonymization");
            }
        } catch (Exception e) {
            log.severe("Error during anonymization and data transfer" + e.getMessage());
            // Send a notification to the development team
            notificationService.sendNotification("Error during anonymization and data transfer", e.getMessage());
        }

    }

    @Override
    public void transferAnonymizedRestaurantData(Restaurant restaurant) throws Exception {

    }
}