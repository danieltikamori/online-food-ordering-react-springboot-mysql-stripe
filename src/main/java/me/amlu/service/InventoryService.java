/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package main.java.me.amlu.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author Daniel Itiro Tikamori
 * @version 1.0
 * @since 1.0
 */

//TODO: Implement Async
@Service
public class InventoryService {

    @Async
    public void updateInventory(Long foodId, int quantityChange) {
        return Thread.ofVirtual().start(() -> {
            // 1. Fetch current inventory from the database
            // 2. Calculate new inventory
            // 3. Update inventory in the database
            return null; // Or return a value if needed
        }).join();
    }
}