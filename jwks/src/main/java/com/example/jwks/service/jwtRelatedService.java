package com.example.jwks.service;

import com.backblaze.b2.client.B2ListFilesIterable;
import com.backblaze.b2.client.structures.B2FileVersion;
import com.backblaze.b2.client.structures.B2ListFileVersionsRequest;
import com.backblaze.b2.client.structures.B2ListFileVersionsResponse;
import com.example.jwks.config.b2Config;
import com.example.jwks.config.certificateConfigs;
import com.example.jwks.config.restTemplateConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.*;
import java.util.Date;

import java.util.List;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.web.client.RestTemplate;

import static io.jsonwebtoken.impl.crypto.EllipticCurveProvider.generateKeyPair;

@Service
public class jwtRelatedService {

private b2Config b2Config;

    private certificateConfigs certificateConfig;

    private publicKeyGeneratorService publicKeyGeneratorService;

    private RestTemplate restTemplateConfig;
    //private final String otherMicroserviceUrl = "http://other-microservice-url/api/resource"; // Replace with actual URL

//    @Autowired
//    public jwtRelatedService(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }

    public jwtRelatedService(com.example.jwks.config.b2Config b2Config, certificateConfigs certificateConfig, com.example.jwks.service.publicKeyGeneratorService publicKeyGeneratorService, RestTemplate restTemplate) {
        this.b2Config = b2Config;
        this.certificateConfig = certificateConfig;
        this.publicKeyGeneratorService = publicKeyGeneratorService;
        this.restTemplateConfig = restTemplate;
    }


    public String generateToken() throws NoSuchAlgorithmException, NoSuchProviderException {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3600000); // 1 hour from now

        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
        generator.initialize(2048);

        KeyPair keyPair = generator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        return Jwts.builder()
                .claim("filename", b2Config.getFileName())
                .claim("bucketId", b2Config.getBucketName())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }



    public String validateToken(String filename, String bucketId) throws Exception {

//       if (b2Config.b2StorageClient().fileNames(bucketId).iterator().toString().contains(filename))
//        return "KID is found in b2 storage";   ;
//
//        return "KID is not found in b2 storage";

        B2ListFileVersionsRequest request = B2ListFileVersionsRequest.builder(bucketId)
                .setStartFileName(filename)
                .build();

        Iterable<B2FileVersion> fileVersionsIterable = b2Config.b2StorageClient().fileVersions(request);

// Iterate through the iterable to process each file version
        StringBuilder result = new StringBuilder();
        boolean found = false;

        for (B2FileVersion fileVersion : fileVersionsIterable) {
            if (fileVersion.getFileName().equals(filename)) {
                result.append("File Name: ").append(fileVersion.getFileName()).append("\n");
                found = true;
            }
        }

        if (!found) {
            result.append("Not found fileName: ").append(filename);
        }

        return result.toString();
    }


}
