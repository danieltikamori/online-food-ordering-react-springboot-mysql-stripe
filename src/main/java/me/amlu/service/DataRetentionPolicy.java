/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.service;

import me.amlu.model.AnonymizedData;

import java.lang.annotation.Annotation;

public interface DataRetentionPolicy {

    int getRetentionDaysBeforeDatabaseRemotion() throws Exception;

    void applyRetentionPolicy(AnonymizedData data) throws Exception;

    void deleteDataAfterRetentionPeriod(AnonymizedData data, int retentionDays) throws Exception;

}
