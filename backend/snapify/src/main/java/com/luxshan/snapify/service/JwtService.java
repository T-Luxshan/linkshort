
package com.luxshan.snapify.service;

import com.luxshan.snapify.model.User;
import com.luxshan.snapify.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;
    private final UserRepository userRepository;

    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Generate signing key
    private SecretKey getSigningKey() {

        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Generate JWT
    public String generateToken(User user){
        Date issuedAt = new Date();

        Date expiration = new Date(
                issuedAt.getTime() + jwtExpiration
        );

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    // Extract claims
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // TODO: Validate JWT

    public boolean isTokenValid(String token, User user) {

        try {
            // Step 1: Extract the user ID from the token
            String tokenUserId = extractUserId(token);

            // Step 2: Get the expected user's ID as a String
            String expectedUserId = user.getId().toString();

            // Step 3: Compare both IDs and return the result
            return tokenUserId.equals(expectedUserId);

        } catch (JwtException | IllegalArgumentException e) {

            // Invalid signature, malformed token, expired token, etc.
            return false;
        }
    }


    public String extractUserId(String token) {
        return extractAllClaims(token).getSubject();
    }

}
