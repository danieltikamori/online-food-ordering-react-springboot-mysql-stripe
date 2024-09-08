package me.amlu.service;

import me.amlu.model.AnonymizedData;

import java.lang.annotation.Annotation;

public interface DataRetentionPolicy {

    int getRetentionDays() throws Exception;

    void applyRetentionPolicy(AnonymizedData data, Annotation annotation) throws Exception;

    void deleteDataAfterRetentionPeriod(AnonymizedData data, int retentionDays) throws Exception;

}
