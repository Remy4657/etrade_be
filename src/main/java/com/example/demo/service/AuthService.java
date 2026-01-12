package com.example.demo.service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.example.demo.dto.req.LoginRequest;
import com.example.demo.dto.req.RegisterRequest;
import com.example.demo.dto.res.AuthGoogleResponse;
import com.example.demo.dto.res.AuthResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    void logout(HttpServletRequest request, HttpServletResponse response);

    void cleanExpiredTokens();

    AuthGoogleResponse loginWithGoogle(Jwt jwt);
}
