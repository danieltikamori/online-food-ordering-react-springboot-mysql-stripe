package me.amlu.service.Tasks;

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
        if (dataRetentionPolicy == null || dataRetentionPolicy.getRetentionDays() == 0) {
            // Do nothing if no retention period is configured
            return;
        }

        Instant threshold = Instant.now().minus(dataRetentionPolicy.getRetentionDays(), ChronoUnit.DAYS);
        userRepository.deleteAllByDeletedAtBefore(threshold);
        orderRepository.deleteAllByDeletedAtBefore(threshold);
    }

}