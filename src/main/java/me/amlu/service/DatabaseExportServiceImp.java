/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.service;

import lombok.NonNull;
import me.amlu.model.Order;
import me.amlu.model.Restaurant;
import me.amlu.model.User;
import me.amlu.repository.RestaurantRepository;
import me.amlu.repository.UserRepository;
import me.amlu.service.Tasks.DataExporter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;

@Service
public class DatabaseExportServiceImp implements DatabaseExportService {

    private final UserRepository userRepository;

    private final DataExporter dataExporter;

    private final S3StorageService s3StorageService;

    private final NotificationService notificationService;

    private static final Logger log = Logger.getLogger(DatabaseExportServiceImp.class.getName());
    private final RestaurantRepository restaurantRepository;

    public DatabaseExportServiceImp(UserRepository userRepository, AnonymizationService anonymizationService, DataExporter dataExporter, S3StorageService s3StorageService, NotificationService notificationService,
                                    RestaurantRepository restaurantRepository) {
        this.userRepository = userRepository;
        this.dataExporter = dataExporter;
        this.s3StorageService = s3StorageService;
        this.notificationService = notificationService;
        this.restaurantRepository = restaurantRepository;
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
                user.getFullName(),
                user.getEmail(),
                user.getRole().name(),
                user.getCreatedAt().toString(), // Format Instant as needed
                user.getCreatedBy() != null ? user.getCreatedBy().getFullName() : "", // Handle null createdBy
                user.getUpdatedAt().toString(), // Format Instant as needed
                user.getUpdatedBy() != null ? user.getUpdatedBy().getFullName() : "", // Handle null updatedBy
                user.getDeletedAt() != null ? user.getDeletedAt().toString() : "", // Handle null deletedAt
                user.getDeletedBy() != null ? user.getDeletedBy().getFullName() : "", // Handle null deletedBy
                user.getAnonymizedAt() != null ? user.getAnonymizedAt().toString() : "", // Handle null anonymizedAt
                user.getAnonymizedBy() != null ? user.getAnonymizedBy().getFullName() : "" // Handle null anonymizedBy
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

    // Helper method to convert a User object to a CSV row (String[])
    private String[] restaurantToCsvRow(Restaurant restaurant) {
        return new String[]{
                restaurant.getRestaurant_id().toString(),
                String.valueOf(restaurant.getOwner()),
                restaurant.getRestaurantName(),
                restaurant.getDescription(),
                restaurant.getCuisineType(),
                String.valueOf(restaurant.getAddress()),
                String.valueOf(restaurant.getContactInformation()),
                restaurant.getOpeningHours(),
                String.valueOf(restaurant.getImages()),
                restaurant.getFoods() != null ? String.valueOf(restaurant.getFoods().size()) : "",
                restaurant.getCreatedAt().toString(), // Format Instant as needed
                restaurant.getCreatedBy() != null ? restaurant.getCreatedBy().getFullName() : "", // Handle null createdBy
                restaurant.getUpdatedAt().toString(), // Format Instant as needed
                restaurant.getUpdatedBy() != null ? restaurant.getUpdatedBy().getFullName() : "", // Handle null updatedBy
                restaurant.getDeletedAt() != null ? restaurant.getDeletedAt().toString() : "", // Handle null deletedAt
                restaurant.getDeletedBy() != null ? restaurant.getDeletedBy().getFullName() : "", // Handle null deletedBy
                restaurant.getAnonymizedAt() != null ? restaurant.getAnonymizedAt().toString() : "", // Handle null anonymizedAt
                restaurant.getAnonymizedBy() != null ? restaurant.getAnonymizedBy().getFullName() : "" // Handle null anonymizedBy
        };
    }

    @Override
    public void exportAnonymizedUserData(@NonNull User user) {
        List<User> anonymizedUsers = fetchAnonymizedUsers();

        try {
            // Define headers for User CSV
            String[] userHeaders = {"ID", "Full Name", "Email", "Role", "Created At", "Created By", "Updated At", "Updated By", "Deleted At", "Deleted By", "Anonymized At", "Anonymized By", "Anonymized Data Exported At"}; // Adjusted headers

            Function<User, String[]> userRowMapper = (userObj) -> new String[]{
                    userObj.getUser_id().toString(),
                    userObj.getFullName(),
                    userObj.getEmail(),
                    userObj.getRole().name(),
                    String.valueOf(userObj.getCreatedAt()),
                    userObj.getCreatedBy() != null ? userObj.getCreatedBy().getFullName() : "",
                    String.valueOf(userObj.getUpdatedAt()),
                    userObj.getUpdatedBy() != null ? userObj.getUpdatedBy().getFullName() : "",
                    userObj.getDeletedAt() != null ? String.valueOf(userObj.getDeletedAt()) : "",
                    userObj.getDeletedBy() != null ? userObj.getDeletedBy().getFullName() : "",
                    userObj.getAnonymizedAt() != null ? String.valueOf(userObj.getAnonymizedAt()) : "",
                    userObj.getAnonymizedBy() != null ? userObj.getAnonymizedBy().getFullName() : "",
                    userObj.getAnonymizedDataExportedAt() != null ? String.valueOf(userObj.getAnonymizedDataExportedAt()) : ""
            };


            // Export to CSV using the row mapper
            ByteArrayInputStream csvStream = dataExporter.exportToCsv(anonymizedUsers, userHeaders, userRowMapper);

            // Upload to S3
            String s3Key = s3StorageService.uploadFileToS3(Instant.now().toString() + "-anonymized-users.csv", csvStream);
            user.setAnonymizedDataExported(true);
            user.setAnonymizedDataExportedAt(Instant.now());
            user.setAnonymizedDataExportedKey(s3Key);
            userRepository.save(user);
            log.info("Anonymized user data exported successfully to S3 key: " + s3Key);

        } catch (IOException e) {
            log.severe(Instant.now().toString() + " - Error exporting data: " + e.getMessage());
            notificationService.sendNotification(Instant.now().toString() +" - Error exporting data - ", e.getMessage());
        }
    }



    @Override
    public void exportAnonymizedRestaurantData(Restaurant restaurant) {
        List<Restaurant> anonymizedRestaurants = fetchAnonymizedRestaurant();

        try {
            // Define headers for Restaurant CSV
            String[] restaurantHeaders = {
                    "ID", "Restaurant Name", "Description", "Cuisine Type",
                    "Opening Hours", "Open Now", "Created At", "Created By",
                    "Updated At", "Updated By", "Deleted At", "Deleted By",
                    "Anonymized At", "Anonymized By", "Anonymized Data Exported At"
            };

            Function<Restaurant, String[]> restaurantRowMapper = (restaurantObj) -> new String[]{
                    restaurantObj.getRestaurant_id().toString(),
                    restaurantObj.getRestaurantName(),
                    restaurantObj.getDescription(),
                    restaurantObj.getCuisineType(),
                    restaurantObj.getOpeningHours(),
                    String.valueOf(restaurantObj.isOpenNow()),
                    restaurantObj.getCreatedAt().toString(),
                    restaurantObj.getCreatedBy() != null ? restaurantObj.getCreatedBy().getFullName() : "",
                    restaurantObj.getUpdatedAt().toString(),
                    restaurantObj.getUpdatedBy() != null ? restaurantObj.getUpdatedBy().getFullName() : "",
                    restaurantObj.getDeletedAt() != null ? restaurantObj.getDeletedAt().toString() : "",
                    restaurantObj.getDeletedBy() != null ? restaurantObj.getDeletedBy().getFullName() : "",
                    restaurantObj.getAnonymizedAt() != null ? restaurantObj.getAnonymizedAt().toString() : "",
                    restaurantObj.getAnonymizedBy() != null ? restaurantObj.getAnonymizedBy().getFullName() : "",
                    restaurantObj.getAnonymizedDataExportedAt() != null ? restaurantObj.getAnonymizedDataExportedAt().toString() : ""
            };
            // Export to CSV using the row mapper
            ByteArrayInputStream csvStream = dataExporter.exportToCsv(anonymizedRestaurants, restaurantHeaders, restaurantRowMapper);

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
        }

    }

    @Override
    public void exportOrderData(Order order) throws Exception {
// TODO: Implement the method

    }



}
