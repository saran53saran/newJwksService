package com.example.jwks.config;

import com.backblaze.b2.client.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class b2Config {

    @Value("${backblaze.b2.accountId}")
    private String accountId;

    @Value("${backblaze.b2.applicationKey}")
    private String applicationKey;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    @Value("${backblaze.b2.bucketName}")
    private String bucketName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Value("${backblaze.b2.fileName}")
    private String fileName;



    @Bean
    public B2StorageClient b2StorageClient() {

        // Define the name for your test bucket
//        String bucketName = "my-test-bucket";
//        String accountId = "005df1922ba0a220000000003";
//        String applicationKey = "K005aeNnQABOje7FoEa33yLIhNrTJDg";

        String userAgent = "My B2 Test Application";
        System.out.println(accountId);
        System.out.println(applicationKey);


        B2StorageClient client = B2StorageClientFactory
                .createDefaultFactory()
                .create(accountId, applicationKey, userAgent);
        return client;
    }
}
