package com.example.topms.controller;

import com.example.topms.service.topMsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@RestController
public class topMsController {

    private final RestTemplate restTemplate;
    private final String otherMicroserviceUrl = "http://localhost:8082/receive-jwt"; // Replace with actual URL

    @Autowired
    public topMsController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/topms/jwtGenerator")
    public String generateJWT() {
        String jwt = null;
        try {
            ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/jwks/jwtGenerator", String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                jwt = response.getBody();
            } else {
                // Handle non-successful status code
                System.out.println("Error generating JWT: " + response.getStatusCodeValue());
            }
        } catch (Exception e) {
            // Handle exceptions
            System.out.println("Exception while generating JWT: " + e.getMessage());
        }
        System.out.println(jwt);

            // Set JWT in the request headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwt);

            // Make a GET request to the other microservice
            HttpEntity<String> entity = new HttpEntity<>(headers);


            ResponseEntity<String> response = restTemplate.exchange
                    (otherMicroserviceUrl, HttpMethod.GET, entity, String.class);

            // Handle the response
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                // Handle non-successful status code
                return "Error calling other microservice: " + response.getStatusCode();
            }

        }


    }


