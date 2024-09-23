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

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Daniel Itiro Tikamori
 * @version 1.0
 * @since 1.0
 */

//TODO: Implement Async
@Service
public class InventoryService {

    @Async
    @Transactional
    public void updateInventory(Long foodId, int quantityChange) throws InterruptedException {
//        TODO: Implement this
//        try{
//        return Thread.ofVirtual().start(() -> {
//            // 1. Fetch current inventory from the database
//            // 2. Calculate new inventory
//            // 3. Update inventory in the database
//            return; // Or return a value if needed
//        }).join();
//        }catch (Exception e){
//            throw new RuntimeException(e);
//        }
    }
}