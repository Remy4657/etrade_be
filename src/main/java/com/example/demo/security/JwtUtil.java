package com.example.demo.security;

import java.util.Date;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.security.Key;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    private final String SECRET = "SECRET_123456789123456789_SECRET_123456789123456789"; // nên dài >= 256 bit
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public String generateToken(String username, Long userId, String email, List<String> roles) {
        return Jwts.builder()
                // định danh chính
                .setSubject(String.valueOf(userId)) // 👈 userId
                // custom claims
                .claim("username", username)
                .claim("email", email)
                .claim("roles", roles)

                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 ngày
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}