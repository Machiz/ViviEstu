package com.ViviEstu.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // Generar token con email, nombre y customerId
    public String generateToken(String email, String name, Long id, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(email)              // Email del usuario
                .claim("name", name)            // Nombre del usuario
                .claim("Id", id) // ID del Customer
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extraer email del token
    public String getEmailFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.getSubject();
    }

    // Extraer nombre del token
    public String getNameFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("name", String.class);
    }

    // Extraer customerId del token
    public String getCustomerIdFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("customerId", String.class);
    }

    // Obtener todos los claims
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractRole(String token) {
        return getAllClaimsFromToken(token).get("role", String.class);
    }

    // Validar token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}