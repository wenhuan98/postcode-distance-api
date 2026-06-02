package com.interview.postcode_distance_api.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    @Value("${jwt.secret}")
    private String jwtSecret;

    private SecretKey secretKey;

    @PostConstruct
    public void init () {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken (String username) {

        Date issuedDate = new Date();
        Date expirationDate = Date.from(issuedDate.toInstant().plusMillis(jwtExpirationMs));

        return Jwts.builder()
                .subject(username)
                .issuedAt(issuedDate)
                .expiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }

    public boolean validateJwtToken (String token) {
        try {
            extractClaims(token);
            return true;
        } catch (ExpiredJwtException expiredJwtException) {
            System.out.println("JWT token is expired: " + expiredJwtException.getMessage());
        } catch (JwtException jwtException) {
            System.out.println("JWT token is invalid: " + jwtException.getMessage());
        }

        return false;
    }

    public String getUsername (String token) {
        return extractClaims(token).getSubject();

    }

    private Claims extractClaims (String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
