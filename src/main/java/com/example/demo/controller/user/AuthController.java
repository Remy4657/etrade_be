package com.example.demo.controller.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.req.LoginRequest;
import com.example.demo.dto.req.RegisterRequest;
import com.example.demo.dto.res.ApiResponse;
import com.example.demo.dto.res.AuthResponse;
import com.example.demo.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    // public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest
    // request) {
    // return ResponseEntity.ok(authService.register(request));
    // }
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            AuthResponse res = authService.register(request);
            return ResponseEntity.ok(res);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(409, e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
