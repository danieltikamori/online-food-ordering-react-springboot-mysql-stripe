/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
 */

package me.amlu.model;

import org.joou.UInteger;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
class AddressTest {

    @Test
    void getAddress_id() {
        Address address = new Address();
        address.setAddress_id(1L);
        assertEquals(1L, address.getAddress_id());
    }

    @Test
    void getVersion() {
        Address address = new Address();
        address.setVersion(UInteger.valueOf(1));
        assertEquals(UInteger.valueOf(1), address.getVersion());
    }

    @Test
    void getIdempotencyKey() {
        Address address = new Address();
        address.setIdempotencyKey("testKey");
        assertEquals("testKey", address.getIdempotencyKey());
    }

    @Test
    void getStreetAddress() {
        Address address = new Address();
        address.setStreetAddress("123 Main St");
        assertEquals("123 Main St", address.getStreetAddress());
    }

    @Test
    void getCity() {
        Address address = new Address();
        address.setCity("New York");
        assertEquals("New York", address.getCity());
    }

    @Test
    void getStateProvince() {
        Address address = new Address();
        address.setStateProvince("NY");
        assertEquals("NY", address.getStateProvince());
    }

    @Test
    void getPostalCode() {
        Address address = new Address();
        address.setPostalCode("10001");
        assertEquals("10001", address.getPostalCode());
    }

    @Test
    void getCountry() {
        Address address = new Address();
        address.setCountry("US");
        assertEquals("US", address.getCountry());
    }

    @Test
    void getCreatedAt() {
        Address address = new Address();
        address.setCreatedAt(Instant.now());
        assertNotNull(address.getCreatedAt());
    }

    @Test
    void getCreatedBy() {
        Address address = new Address();
        address.setCreatedBy(new User());
        assertNotNull(address.getCreatedBy());
    }

    @Test
    void getUpdatedAt() {
        Address address = new Address();
        address.setUpdatedAt(Instant.now());
        assertNotNull(address.getUpdatedAt());
    }

    @Test
    void getUpdatedBy() {
        Address address = new Address();
        address.setUpdatedBy(new User());
        assertNotNull(address.getUpdatedBy());
    }

    @Test
    void getDeletedAt() {

    }

    @Test
    void getDeletedBy() {
    }

    @Test
    void setAddress_id() {
    }

    @Test
    void setVersion() {
    }

    @Test
    void setIdempotencyKey() {
    }

    @Test
    void setStreetAddress() {
    }

    @Test
    void setCity() {
    }

    @Test
    void setStateProvince() {
    }

    @Test
    void setPostalCode() {
    }

    @Test
    void setCountry() {
    }

    @Test
    void setCreatedAt() {
    }

    @Test
    void setCreatedBy() {
    }

    @Test
    void setUpdatedAt() {
    }

    @Test
    void setUpdatedBy() {
    }

    @Test
    void setDeletedAt() {
    }

    @Test
    void setDeletedBy() {
    }
}