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
import com.example.demo.dto.res.BaseResponse;
import com.example.demo.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
            return new ResponseEntity<>(new BaseResponse<>("Successful", res, 200), HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new BaseResponse<>(e.getMessage(), 409));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        // return ResponseEntity.ok(authService.login(request));
        try {
            AuthResponse res = authService.login(request);
            System.out.println("[controller] res login: " + res);
            Cookie cookie = new Cookie("access_token", res.getToken());
            cookie.setHttpOnly(true);
            cookie.setPath("/");

            cookie.setMaxAge(24 * 60 * 60);

            response.addCookie(cookie);
            return new ResponseEntity<>(new BaseResponse<>("Successful", res, 200), HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new BaseResponse<>(e.getMessage(), 409));
        }
    }

}
