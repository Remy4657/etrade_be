package com.example.demo.security;

import java.util.Date;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Component;

import com.example.demo.config.JwtConfig;
import com.example.demo.entity.user.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtConfig jwtConfig;
    private final String SECRET = "SECRET123456789123456789SECRET123456789123456789"; // nên dài >= 256 bit
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)); // tạo key từ secret

    public String generateAccessToken(String username, Long userId, String email, List<String> roles) {
        return Jwts.builder()
                // định danh chính
                .setSubject(String.valueOf(userId)) // userId
                // custom claims
                .claim("username", username)
                .claim("email", email)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig
                        .getAccessTokenExpiration()
                        * 1000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(UserEntity user) {

        return Jwts.builder()
                .setSubject(String.valueOf(
                        user.getEmail())) // có thể để email hoặc userId tùy nhu cầu, ở đây để email cho dễ debug
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getRefreshTokenExpiration() * 1000))
                .signWith(
                        key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ===============================
    // 1. Lấy username từ token
    // ===============================
    // public String getUsername(String token) {
    // return getClaims(token).getSubject();
    // }

    // 2. Validate token (bao gồm cả check hết hạn)
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaims(token);
            Date expiration = claims.getExpiration();
            // check hết hạn
            return expiration.before(new Date());

        } catch (Exception e) {
            return false;
        }
    }

    // Lấy claims (bao gồm cả exp, iat, sub, custom claims)
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Lấy expiration
    public LocalDateTime getExpiration(String token) {
        Date exp = extractAllClaims(token).getExpiration();
        return exp.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
