package com.example.bootjarAuth.global.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private final Long expiration;
    private final SecretKey secretKey;
    private final Long qrExpiration;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") Long expiration,
            @Value("${qrJwt.expiration}") Long qrExpiration
    ) {
        this.expiration = expiration;
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.qrExpiration = qrExpiration;
    }

    public String generateToken(Long id, String email, String nickname, String image){
        return Jwts.builder()
                .subject(email)
                .claim("email", email)
                .claim("id", id)
                .claim("nickname",nickname)
                .claim("image", image)
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }

    public String generateQRToken(String email) {
        return Jwts.builder()
                .subject(email)
                .claim("email", email)
                .expiration(new Date(System.currentTimeMillis() + qrExpiration))
                .signWith(secretKey)
                .compact();
    }

    public String getByEmailFromTokenAndValidate(String token){
        Claims payload = (Claims) Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parse(token)
                .getPayload();
        return payload.getSubject();
    }
}
