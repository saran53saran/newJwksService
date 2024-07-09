package com.example.jwks.service;

import com.backblaze.b2.client.contentSources.B2ContentTypes;
import com.backblaze.b2.client.exceptions.B2Exception;
import com.backblaze.b2.client.structures.B2Bucket;
import com.backblaze.b2.client.structures.B2ListBucketsResponse;
import com.backblaze.b2.client.structures.B2UploadFileRequest;
import com.example.jwks.config.b2Config;
import org.springframework.stereotype.Service;
import com.backblaze.b2.client.contentSources.*;

import java.util.List;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.cert.CertificateException;

import static java.awt.SystemColor.text;

@Service
public class BackblazeB2Service {

    public BackblazeB2Service(com.example.jwks.config.b2Config b2Config, com.example.jwks.service.publicKeyGeneratorService publicKeyGeneratorService) {
        this.b2Config = b2Config;
        this.publicKeyGeneratorService = publicKeyGeneratorService;
    }

    private b2Config b2Config;
    private final publicKeyGeneratorService publicKeyGeneratorService;




    public void createTestBucket() throws B2Exception {


        b2Config.b2StorageClient().createBucket("saran53saran", "allPrivate");



    }

    public void uploadFile() throws CertificateException, B2Exception, InstantiationException, IllegalAccessException {
        String publicKey = publicKeyGeneratorService.publicKeyGenerator();
        String filename = "public_key2.txt";
     String contentType = "text/plain";

     String bucketId = "0dafc129f2126bda900a0212";

        // Retrieve a list of buckets associated with the account
      //  List<B2Bucket> buckets = (List<B2Bucket>) b2Config.b2StorageClient().listBuckets();

//        // Retrieve a list of buckets associated with the account
//        B2ListBucketsResponse response = b2Config.b2StorageClient().listBuckets();
//
//        // Get the list of B2Bucket objects from the response
//        List<B2Bucket> buckets = response.getBuckets();
//
//        // Iterate through the list to find the matching bucketName and return its bucketId
//        for (B2Bucket bucket : buckets) {
//            String bucketId = null;
//            if (bucket.getBucketName().equals("jwkstestbucket")) {
//                bucketId = bucket.getBucketId();
//            }



            byte[] contentBytes = publicKey.getBytes(StandardCharsets.UTF_8);
            B2ByteArrayContentSource contentSource = (B2ByteArrayContentSource) B2ByteArrayContentSource.build(contentBytes);


            B2UploadFileRequest request = B2UploadFileRequest.builder(bucketId, filename, contentType, contentSource)
                    .build();

            b2Config.b2StorageClient().uploadSmallFile(request);



}}

