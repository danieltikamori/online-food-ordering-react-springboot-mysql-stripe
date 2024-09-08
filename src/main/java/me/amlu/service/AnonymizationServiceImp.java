package me.amlu.service;

import me.amlu.model.Restaurant;
import me.amlu.model.User;
import me.amlu.repository.RestaurantRepository;
import me.amlu.repository.UserRepository;
import me.amlu.service.Tasks.RandomValueGenerator;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.logging.Logger;

@Service
public class AnonymizationServiceImp implements AnonymizationService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final NotificationService notificationService;



    private static final Logger log = Logger.getLogger(AnonymizationServiceImp.class.getName());

    public AnonymizationServiceImp(UserRepository userRepository, RestaurantRepository restaurantRepository, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.notificationService = notificationService;
    }

    @Override
    public void anonymizeUser(User user) {
        try {
            if (user.getDeletedAt() != null) {
                // Pseudonymize the user name
                user.setFullName("Anonymized User");

                // De-identify the user email with a random value
                String newEmail;
                do {
                    newEmail = RandomValueGenerator.generateRandomValue() + "@anonymizedUserEmail.com";
                } while (userRepository.existsByEmail(newEmail)); // Check if email already exists

                // Update the user's email with the new value
                user.setEmail(newEmail);

//            // Mask the user password
//            user.setPassword("********");

                user.setAddresses(null);
                user.setFavoriteRestaurants(null);
                user.setCreatedBy(user);
                user.setAnonymizedAt(Instant.now());
                user.setAnonymizedBy(user);

                // Save the anonymized user to the database
                userRepository.save(user);
                log.info(Instant.now().toString() + " - User anonymized successfully");
            }
            // Return true to indicate that the user was successfully anonymized
        } catch (Exception e) {
            // Log the exception and return false to indicate that the anonymization failed
            log.severe(Instant.now().toString() + " - Error anonymizing user: " + e.getMessage());
            notificationService.sendNotification(Instant.now().toString() + " - Error anonymizing user: ", e.getMessage());
        }
    }

    @Override
    public void anonymizeRestaurant(Restaurant restaurant) {
        try {
            // Pseudonymize the restaurant name
            restaurant.setOwner(null);
            restaurant.setRestaurantName("Anonymized Restaurant");
            restaurant.setCuisineType(null);
            restaurant.setAddress(null);
            restaurant.setDescription(null);
            restaurant.setContactInformation(null);
            restaurant.setOpeningHours(null);
            restaurant.setImages(null);
            restaurant.setOpenNow(false);
            restaurant.setAnonymizedAt(Instant.now());
            restaurant.setAnonymizedBy((restaurant.getOwner()));


            // Save the anonymized restaurant to the database
            restaurantRepository.save(restaurant);
            log.info(Instant.now().toString() + " - Restaurant anonymized successfully");
            // Return true to indicate that the restaurant was successfully anonymized
        } catch (Exception e) {
            // Log the exception and return false to indicate that the anonymization failed
            log.severe(Instant.now().toString() + " - Error anonymizing restaurant: " + e.getMessage());
            notificationService.sendNotification(Instant.now().toString() + " - Error anonymizing restaurant: ", e.getMessage());
        }
    }


    @Override
    public boolean isUserAnonymized(User user) {
        return user.getFullName().equals("Anonymized User")
                && user.getEmail() != null && !user.getEmail().isEmpty()
                && user.getEmail().endsWith("@anonymizedUserEmail.com")
                && user.getPassword().equals("********");
    }

    @Override
    public boolean isRestaurantAnonymized(Restaurant restaurant) {
        return restaurant.getOwner() == null && restaurant.getRestaurantName().equals("Anonymized Restaurant") &&
                restaurant.getAddress() == null &&
                restaurant.getDescription() == null &&
                restaurant.getContactInformation() == null &&
                restaurant.getImages() == null;
    }
}