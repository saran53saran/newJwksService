package com.example.samplebottomms;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
public class jwtReceiverController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/receive-jwt")
    public ResponseEntity<String> receiveJwt(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) throws JsonProcessingException {
        // Extract JWT token from Authorization header
        String jwtToken = authorizationHeader.replace("Bearer ", "");

        // Process JWT token as needed
        System.out.println("Received JWT token: " + jwtToken);

        String[] jwtParts = jwtToken.split("\\.");
        if (jwtParts.length != 3) {
            System.out.println("Invalid JWT format");
            return null;
        }

        // Decode the payload (second part)
        String base64EncodedPayload = jwtParts[1];
        String payload = new String(Base64.getUrlDecoder().decode(base64EncodedPayload), StandardCharsets.UTF_8);

        // Print the decoded payload (claims)
        System.out.println("Decoded JWT claims:");
        System.out.println(payload);


        // Return a response (if needed)
      //  return ResponseEntity.ok("Received JWT token: " + jwtToken + payload);

        // Parse JSON payload into JSON node
        ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(payload);

            // Extract values
            String filename1 = jsonNode.get("filename").asText();
            String bucketId = jsonNode.get("bucketId").asText();

       String filename = filename1.replaceAll("\"", "");

       // String filename = "test";
            // Print values
            System.out.println("filename: " + filename);
            System.out.println("bucketId: " + bucketId);



            String url = "http://localhost:8080/jwks/validateJwt";

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("filename", filename);
        requestBody.add("bucketId", bucketId);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create HttpEntity with headers and body
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Send POST request
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class);

        // Handle response as needed
            return ResponseEntity.ok("Jwks service validation is completed: " + response.getBody());
    }
}
