/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.model;

import org.joou.UInteger;
import org.junit.jupiter.api.Test;

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
        address.setVersion(UInteger.valueOf(1L));
        assertEquals(1L, address.getVersion());
    }

    @Test
    void getIdempotencyKey() {
    }

    @Test
    void getStreetAddress() {
    }

    @Test
    void getCity() {
    }

    @Test
    void getStateProvince() {
    }

    @Test
    void getPostalCode() {
    }

    @Test
    void getCountry() {
    }

    @Test
    void getCreatedAt() {
    }

    @Test
    void getCreatedBy() {
    }

    @Test
    void getUpdatedAt() {
    }

    @Test
    void getUpdatedBy() {
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