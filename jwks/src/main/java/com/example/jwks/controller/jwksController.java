package com.example.jwks.controller;

import com.backblaze.b2.client.exceptions.B2Exception;
import com.example.jwks.service.BackblazeB2Service;
import com.example.jwks.service.jwtRelatedService;
import com.example.jwks.service.publicKeyGeneratorService;
import org.springframework.web.bind.annotation.*;

import java.security.cert.CertificateException;

@RestController
public class jwksController {

    private final BackblazeB2Service BackblazeB2Service;

    private final publicKeyGeneratorService publicKeyGeneratorService;

    private final jwtRelatedService jwtRelatedService;

    public jwksController(com.example.jwks.service.BackblazeB2Service backblazeB2Service, com.example.jwks.service.publicKeyGeneratorService publicKeyGeneratorService, com.example.jwks.service.jwtRelatedService jwtRelatedService) {
        this.BackblazeB2Service = backblazeB2Service;
        this.publicKeyGeneratorService = publicKeyGeneratorService;
        this.jwtRelatedService = jwtRelatedService;
    }


    @GetMapping("/hello")
    public String Hello(){
return "hello";
    }


    @GetMapping("/jwks/publickey")
    public String generatePublicKey() throws Exception {
        // Remove header, footer, and line breaks from certificate string
        return publicKeyGeneratorService.publicKeyGenerator();

    }

    @GetMapping("/jwks/createbucket")
    public String createBucket() throws B2Exception {
        BackblazeB2Service.createTestBucket();
        return "successful";
    }

    @GetMapping("/jwks/uploadfile")
    public String uploadFile() throws B2Exception, CertificateException, InstantiationException, IllegalAccessException {
        BackblazeB2Service.uploadFile();
        return "successful";
    }

    @GetMapping("/jwks/jwtGenerator")
    public String jwtGenerator() throws Exception {
        // Remove header, footer, and line breaks from certificate string
        return jwtRelatedService.generateToken();

    }

    @PostMapping("/jwks/validateJwt")
    public String callingOtherMicroservice(@RequestParam String filename,
                                           @RequestParam String bucketId) throws Exception {
        // Remove header, footer, and line breaks from certificate string
        return jwtRelatedService.validateToken(filename, bucketId);

    }


}
