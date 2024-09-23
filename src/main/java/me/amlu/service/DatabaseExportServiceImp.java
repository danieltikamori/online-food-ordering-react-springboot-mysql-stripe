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
import me.amlu.model.Order;
import me.amlu.model.Restaurant;
import me.amlu.model.User;
import me.amlu.repository.OrderRepository;
import me.amlu.repository.RestaurantRepository;
import me.amlu.repository.UserRepository;
import me.amlu.service.tasks.DataExporter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

@Service
public class DatabaseExportServiceImp implements DatabaseExportService {

    private final UserRepository userRepository;

    private final me.amlu.service.tasks.DataExporter dataExporter;

    private final S3StorageService s3StorageService;

    private final NotificationService notificationService;

    private final RestaurantRepository restaurantRepository;

    private final OrderRepository orderRepository;

    private static final Logger log = Logger.getLogger(DatabaseExportServiceImp.class.getName());


    public DatabaseExportServiceImp(UserRepository userRepository, AnonymizationService anonymizationService, DataExporter dataExporter, S3StorageService s3StorageService, NotificationService notificationService,
                                    RestaurantRepository restaurantRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.dataExporter = dataExporter;
        this.s3StorageService = s3StorageService;
        this.notificationService = notificationService;
        this.restaurantRepository = restaurantRepository;
        this.orderRepository = orderRepository;
    }

    private List<User> fetchAnonymizedUsers() {
        List<User> users = userRepository.findAll();
        List<User> anonymizedUsers = new ArrayList<>();

        for (User user : users) {
            // Check if the user is anonymized and has not been anonymized yet
            if (user.getAnonymizedAt() != null && !user.isAnonymizedDataExported()) {
                anonymizedUsers.add(user);
            }
        }

        return anonymizedUsers;
    }

    // Helper method to convert a User object to a CSV row (String[])
    private String[] userToCsvRow(User user) {
        return new String[]{
                user.getUser_id().toString(),
                user.getVersion() != null ? user.getVersion().toString() : "",
                user.getIdempotencyKey(),
                user.getFullName(),
                user.getEmail(),
                user.getPassword(),
                user.getPhoneNumber() != null ? user.getPhoneNumber().toString() : "",
                user.getRole().name(),
                user.getOrders() != null ? String.valueOf(user.getOrders().size()) : "",
                user.getFavoriteRestaurants() != null ? String.valueOf(user.getFavoriteRestaurants().size()) : "",
                user.getAddresses() != null ? String.valueOf(user.getAddresses().size()) : "",
                user.getCreatedAt().toString(), // Format Instant as needed
                user.getCreatedBy() != null ? user.getCreatedBy().getFullName() : "", // Handle null createdBy
                user.getUpdatedAt().toString(), // Format Instant as needed
                user.getUpdatedBy() != null ? user.getUpdatedBy().getFullName() : "", // Handle null updatedBy
                user.getDeletedAt() != null ? user.getDeletedAt().toString() : "", // Handle null deletedAt
                user.getDeletedBy() != null ? user.getDeletedBy().getFullName() : "", // Handle null deletedBy
                user.getAnonymizedAt() != null ? user.getAnonymizedAt().toString() : "", // Handle null anonymizedAt
                user.getAnonymizedBy() != null ? user.getAnonymizedBy().getFullName() : "", // Handle null anonymizedBy
                user.getUserDataExportedAt() != null ? user.getUserDataExportedAt().toString() : "", // Handle null userDataExportedAt
                user.isUserDataExported() ? "true" : "false",
                user.getUserDataExportedKey()
        };
    }

    private List<Restaurant> fetchAnonymizedRestaurant() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        List<Restaurant> anonymizedRestaurants = new ArrayList<>();

        for (Restaurant restaurant : restaurants) {
            // Check if the user is anonymized and has not been anonymized yet
            if (restaurant.getAnonymizedAt() != null && !restaurant.isAnonymizedDataExported()) {
                anonymizedRestaurants.add(restaurant);
            }
        }

        return anonymizedRestaurants;
    }

    // Helper method to convert a Restaurant object to a CSV row (String[])
    private String[] restaurantToCsvRow(Restaurant restaurant) {
        return new String[]{
                restaurant.getRestaurant_id().toString(),
                restaurant.getVersion() != null ? restaurant.getVersion().toString() : "",
                restaurant.getIdempotencyKey(),
                String.valueOf(restaurant.getOwner().getUser_id()),
                restaurant.getRestaurantStaff() != null ? String.valueOf(restaurant.getRestaurantStaff().size()) : "",
                restaurant.getRestaurantName(),
                restaurant.getDescription(),
                restaurant.getCuisineType(),
                String.valueOf(restaurant.getAddress()),
                String.valueOf(restaurant.getContactInformation()),
                restaurant.getOpeningHours(),
                restaurant.getOrders() != null ? String.valueOf(restaurant.getOrders().size()) : "",
                String.valueOf(restaurant.getImagesURL()),
                restaurant.isOpenNow() ? "true" : "false",
                restaurant.getFoods() != null ? String.valueOf(restaurant.getFoods().size()) : "",
                restaurant.getCreatedAt().toString(), // Format Instant as needed
                restaurant.getCreatedBy() != null ? restaurant.getCreatedBy().getFullName() : "", // Handle null createdBy
                restaurant.getUpdatedAt().toString(), // Format Instant as needed
                restaurant.getUpdatedBy() != null ? restaurant.getUpdatedBy().getFullName() : "", // Handle null updatedBy
                restaurant.getDeletedAt() != null ? restaurant.getDeletedAt().toString() : "", // Handle null deletedAt
                restaurant.getDeletedBy() != null ? restaurant.getDeletedBy().getFullName() : "", // Handle null deletedBy
                restaurant.getAnonymizedAt() != null ? restaurant.getAnonymizedAt().toString() : "", // Handle null anonymizedAt
                restaurant.getAnonymizedBy() != null ? restaurant.getAnonymizedBy().getFullName() : "", // Handle null anonymizedBy
                restaurant.getRestaurantDataExportedAt() != null ? restaurant.getRestaurantDataExportedAt().toString() : "", // Handle null userDataExportedAt
                restaurant.isRestaurantDataExported() ? "true" : "false",
                restaurant.getRestaurantDataExportedKey()
        };
    }

    @Override
    public void exportAnonymizedUserData(@NonNull User user, String exportFormat) {
        List<User> anonymizedUsers = fetchAnonymizedUsers();

        try {
            // Define headers for User CSV
            ByteArrayInputStream csvStream = dataExporter.exportToCsv(anonymizedUsers, this::userToCsvRow);

            // Upload to S3
            String s3Key = s3StorageService.uploadFileToS3(Instant.now().toString() + "-anonymized-users.csv", csvStream);
            user.setAnonymizedDataExported(true);
            user.setAnonymizedDataExportedAt(Instant.now());
            user.setAnonymizedDataExportedKey(s3Key);
            userRepository.save(user);
            log.info("Anonymized user data exported successfully to S3 key: " + s3Key);

        } catch (IOException e) {
            log.severe(Instant.now().toString() + " - Error exporting data: " + e.getMessage());
            notificationService.sendNotification(Instant.now().toString() + " - Error exporting data - ", e.getMessage());
        }
    }

    @Override
    public void exportUserData(User user, String exportFormat) throws Exception {
        CopyOnWriteArrayList<User> users = (CopyOnWriteArrayList<User>) userRepository.findAll();
        CopyOnWriteArrayList<User> usersToExport = new CopyOnWriteArrayList<>();

        for (User usr : users) {
            if (!usr.isUserDataExported()) {
                usersToExport.add(usr);
            }
        }

        try {
            ByteArrayInputStream csvStream = dataExporter.exportToCsv(users, this::userToCsvRow);

            // Upload to S3
            String s3Key = s3StorageService.uploadFileToS3(Instant.now().toString() + "-users.csv", csvStream);

            // Update order export status
            for (User usr : usersToExport) {
                usr.setUserDataExported(true);
                usr.setUserDataExportedAt(Instant.now());
                usr.setUserDataExportedKey(s3Key);
                userRepository.save(usr);
            }

            log.info("Order data exported successfully to S3 key: " + s3Key);

        } catch (IOException e) {
            log.severe(Instant.now().toString() + " - Error exporting order data: " + e.getMessage());
            notificationService.sendNotification(Instant.now().toString() + " - Error exporting order data - ", e.getMessage());
        }

    }


    @Override
    public void exportAnonymizedRestaurantData(Restaurant restaurant, String exportFormat) {
        List<Restaurant> anonymizedRestaurants = fetchAnonymizedRestaurant();

        try {
            ByteArrayInputStream csvStream = dataExporter.exportToCsv(anonymizedRestaurants, this::restaurantToCsvRow);

            // Upload to S3
            String s3Key = s3StorageService.uploadFileToS3(Instant.now().toString() + "-anonymized-restaurants.csv", csvStream);
            restaurant.setAnonymizedDataExported(true);
            restaurant.setAnonymizedDataExportedAt(Instant.now());
            restaurant.setAnonymizedDataExportedKey(s3Key);
            restaurantRepository.save(restaurant);
            log.info("Anonymized restaurant data exported successfully to S3 key: " + s3Key);


        } catch (IOException e) {
            log.severe(Instant.now().toString() + " - Error exporting data: " + e.getMessage());
            notificationService.sendNotification(Instant.now().toString() + " - Error exporting data - ", e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public void exportRestaurantData(Restaurant restaurant, String exportFormat) throws Exception {
        CopyOnWriteArrayList<Restaurant> restaurants = (CopyOnWriteArrayList<Restaurant>) restaurantRepository.findAll();

        try {
            ByteArrayInputStream csvStream = dataExporter.exportToCsv(restaurants, this::restaurantToCsvRow);

            // Upload to S3
            String s3Key = s3StorageService.uploadFileToS3(Instant.now().toString() + "-restaurants.csv", csvStream);
            restaurant.setRestaurantDataExported(true);
            restaurant.setRestaurantDataExportedAt(Instant.now());
            restaurant.setRestaurantDataExportedKey(s3Key);
            restaurantRepository.save(restaurant);
            log.info(Instant.now().toString() + " - Restaurant data exported successfully to S3 key: " + s3Key);

        } catch (IOException e) {
            log.severe(Instant.now().toString() + " - Error exporting restaurant data: " + e.getMessage());
            notificationService.sendNotification(Instant.now().toString() + " - Error exporting restaurant data - ", e.getMessage());
        }

    }

    // Helper methods to convert objects to CSV rows (String[])

    private String[] orderToCsvRow(Order order) {
        return new String[]{
                order.getOrder_id().toString(),
                order.getVersion() != null ? order.getVersion().toString() : "",
                order.getIdempotencyKey(),
                order.getCustomer().getUser_id().toString(), // Export customer ID
                order.getRestaurant().getRestaurant_id().toString(), // Export restaurant ID
                order.getOrderStatus().name(),
                String.valueOf(order.getDeliveryAddress()), // Export delivery address
                String.valueOf(order.getOrderItems()), // Export order items
                String.valueOf(order.getTotalItems()),
                order.getTotalAmount().toString(),
                order.getCreatedAt().toString(), // Format Instant as needed
                order.getCreatedBy() != null ? String.valueOf(order.getCreatedBy().getUser_id()) : "", // Handle null createdBy
                order.getUpdatedAt().toString(), // Format Instant as needed
                order.getUpdatedBy() != null ? String.valueOf(order.getUpdatedBy().getUser_id()) : "", // Handle null updatedBy
                order.getDeletedAt() != null ? order.getDeletedAt().toString() : "", // Handle null deletedAt
                order.getDeletedBy() != null ? String.valueOf(order.getDeletedBy().getUser_id()) : "", // Handle null deletedBy
                order.getOrderDataExportedAt() != null ? order.getOrderDataExportedAt().toString() : "", // Handle null userDataExportedAt
                order.isOrderDataExported() ? "true" : "false",
                order.getOrderDataExportedKey()
                // ... (other fields, matching the order in getHeadersFromEntity) ...
        };
    }

    @Override
    public void exportOrderData(Order order) throws Exception {
        List<Order> orders = orderRepository.findAll(); // Fetch all orders

        try {
            ByteArrayInputStream csvStream = dataExporter.exportToCsv(orders, this::orderToCsvRow);

            // Upload to S3
            String s3Key = s3StorageService.uploadFileToS3(Instant.now().toString() + "-orders.csv", csvStream);

            // Update order export status
            for (Order ord : orders) {
                ord.setOrderDataExported(true);
                ord.setOrderDataExportedAt(Instant.now());
                ord.setOrderDataExportedKey(s3Key);
                orderRepository.save(ord);
            }

            log.info("Order data exported successfully to S3 key: " + s3Key);

        } catch (IOException e) {
            log.severe(Instant.now().toString() + " - Error exporting order data: " + e.getMessage());
            notificationService.sendNotification(Instant.now().toString() + " - Error exporting order data - ", e.getMessage());
        }
    }
}