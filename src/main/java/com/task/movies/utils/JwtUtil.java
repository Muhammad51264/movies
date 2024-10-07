package com.task.movies.utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${security.jwt.expiration-time}")
    private String jwtIssuer;

    private final Key key;

    // Token validity in milliseconds (e.g., 24 hours)
    private final long jwtExpirationMs = 86400000;

    public JwtUtil() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Generate a secure key
    }

    // Generate JWT token
    public String generateToken(String email, Long id) {
        return Jwts.builder()
                .setSubject(email) // email as the subject
                .claim("id", id)   // user ID as a custom claim
                .setIssuer(jwtIssuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    // Get username from token
    public Long getUserIdFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.get("id", Long.class);
    }

    public String getEmailFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    // Validate JWT token
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            // Log the exception (optional)
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
