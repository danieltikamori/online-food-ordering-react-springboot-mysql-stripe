package me.amlu.service;

import me.amlu.model.User;
import org.springframework.stereotype.Service;
import java.util.logging.Logger;

@Service
public class AnonymizationServiceImp implements AnonymizationService {

    private static final Logger log = Logger.getLogger(AnonymizationServiceImp.class.getName());
    
    @Override
    public boolean anonymizeUser(User user) {
        try {
            // Pseudonymize the user name
            user.setFullName("Anonymized User");

            // De-identify the user email
            user.setEmail("anonymized@example.com");

            // Mask the user password
            user.setPassword("********");

            // Return true to indicate that the user was successfully anonymized
            return true;
        } catch (Exception e) {
            // Log the exception and return false to indicate that the anonymization failed
            log.severe("Error anonymizing user: " + e.getMessage());
            return false;
        }
    }
}