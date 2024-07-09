package com.example.topms.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class topMsService {

//
//    public String generateToken(String filename, String bucketId, String PrivateKey) {
//
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + 3600000); // 1 hour from now
//
//        return Jwts.builder()
//                .claim("filename", filename)
//                .claim("bucketId", bucketId)
//                .setIssuedAt(now)
//                .setExpiration(expiryDate)
//                .signWith(SignatureAlgorithm.RS256, PrivateKey)
//                .compact();
//    }

}
