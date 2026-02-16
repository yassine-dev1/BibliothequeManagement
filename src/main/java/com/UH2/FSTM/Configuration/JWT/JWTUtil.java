package com.UH2.FSTM.Configuration.JWT;

import java.util.Date;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JWTUtil {
    

    private static final String SECRET = "ma-cle-super-secrete-pour-jwt-123456789-fstm";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    
    // generate token
    public static String generateToken(String username, String role) {
        return Jwts.builder()
                .subject(username) 
                .claim("role", role)
                .issuedAt(new Date()) 
                .expiration(new Date(System.currentTimeMillis() + 3600000)) // 1h
                .signWith(KEY) 
                .compact();
    }
    
    // validate token
    public static Claims validateToken(String token) {
        return Jwts.parser() 
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token) 
                .getPayload(); 
    }
}