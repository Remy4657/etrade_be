package com.example.demo.controller.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.req.LoginRequest;
import com.example.demo.dto.req.RegisterRequest;
import com.example.demo.dto.res.ApiResponse;
import com.example.demo.dto.res.AuthGoogleResponse;
import com.example.demo.dto.res.AuthResponse;
import com.example.demo.dto.res.BaseResponse;
import com.example.demo.entity.user.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    @PostMapping("/register")
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

    @GetMapping("/me")
    public UserEntity me(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletRequest request,
            HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.ok("Logout success");
    }

    @PostMapping("/google")
    public ResponseEntity<?> loginGoogle(
            @AuthenticationPrincipal Jwt jwt, HttpServletResponse response) {
        // return ResponseEntity.ok(
        // authService.loginWithGoogle(jwt));
        try {
            AuthGoogleResponse res = authService.loginWithGoogle(jwt);
            System.out.println("[controller login google] res login: " + res);
            Cookie cookie = new Cookie("access_token", res.getAccessToken());
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
