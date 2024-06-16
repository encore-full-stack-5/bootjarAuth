package com.example.bootjarAuth.global.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private final Long expiration;
    private final SecretKey secretKey;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") Long expiration
    ) {
        this.expiration = expiration;
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String email){
        return Jwts.builder()
                .subject(email)
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }

//    public String generateToken(String email){
//        String token = Jwts.builder()
//                .subject(email)
//                .expiration(new Date(System.currentTimeMillis() + expiration))
//                .signWith(secretKey)
//                .compact();
//        return token;
//    }
//
//    public String getByEmailFromTokenAndValidate(String token){
//        Claims payload = (Claims) Jwts.parser()
//                .verifyWith(secretKey)
//                .build()
//                .parse(token)
//                .getPayload();
//        return payload.getSubject();
//    }
//
//
//    public JwtUtil(
//            @Value("${jwt.secret}") String secret,
//            @Value("${jwt.expiration}") Long expiration
//    ) {
//        this.expiration = expiration;
//        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
//    }
}
