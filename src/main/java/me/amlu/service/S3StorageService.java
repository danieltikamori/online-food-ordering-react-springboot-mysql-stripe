/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.service;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.InputStream;

@Service
public class S3StorageService {

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public String uploadFileToS3(String fileName, InputStream inputStream) {
        AWSCredentialsProvider credentialsProvider = DefaultAWSCredentialsProviderChain.getInstance();
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion(Regions.US_EAST_1) // Replace with your S3 region
                .build();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("text/csv"); // Set content type to CSV

        s3Client.putObject(bucketName, fileName, inputStream, metadata);
        return fileName;
    }
}