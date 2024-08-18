package me.amlu.service;

import lombok.NonNull;
import me.amlu.model.User;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

@Service
public class DataTransferServiceImp implements DataTransferService {

    private final AnonymizationService anonymizationService;

    private final DataRetentionPolicy dataRetentionPolicy;

    private final NotificationService notificationService;

//    private final DataTransferService dataTransferService;

    private static final Logger log = Logger.getLogger(DataTransferServiceImp.class.getName());

    public DataTransferServiceImp(AnonymizationService anonymizationService, NotificationService notificationService, DataRetentionPolicy dataRetentionPolicy) {
        this.anonymizationService = anonymizationService;
        this.dataRetentionPolicy = dataRetentionPolicy;
        this.notificationService = notificationService;
//        this.dataTransferService = dataTransferService;
    }

    @Override
    public void exportData(@NonNull User user) {

        // Export the data to a file
        try (FileOutputStream fos = new FileOutputStream("anonymized_data_" + user.getId() + ".txt")) {
            fos.write(user.toString().getBytes());
        } catch (IOException e) {
// Handle the exception
            log.severe("Error exporting data: " + e.getMessage());
            // Send a notification to the development team
            notificationService.sendNotification("Error exporting data", e.getMessage());
        }
        }



    @Override
    public void transferData(User user) throws Exception {

        exportData(user);
        try {
            boolean isAnonymized = anonymizationService.anonymizeUser(user);
            if (isAnonymized) {
                //Transfer the data to a third-party service or storage
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
}