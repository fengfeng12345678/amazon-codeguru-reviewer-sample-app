package com.shipmentEvents.util;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class S3ClientUtil {
    // Testing code changes 
    public static AmazonS3 getS3Client() {
        return AmazonS3ClientBuilder.standard().withRegion(Regions.DEFAULT_REGION).build();
    }
    
}
