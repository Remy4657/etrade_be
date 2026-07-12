package com.example.demo.controller;

import java.util.Map;

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

import com.example.demo.config.JwtConfig;
import com.example.demo.dto.req.LoginRequest;
import com.example.demo.dto.req.RegisterRequest;
import com.example.demo.dto.res.AuthGoogleResponse;
import com.example.demo.dto.res.AuthResponse;
import com.example.demo.dto.res.BaseResponse;
import com.example.demo.dto.res.SignupResponse;
import com.example.demo.entity.user.UserEntity;
import com.example.demo.exception.AppExceptions.UnauthorizedError;
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
    private final JwtConfig jwtConfig;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        SignupResponse res = authService.register(request);
        return new ResponseEntity<>(new BaseResponse<>("Đăng ký thành công", res, 200), HttpStatus.OK);

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {

        AuthResponse res = authService.login(request);
        // set access token
        Cookie accessCookie = new Cookie("access_token", res.getAccessToken());
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge((int) jwtConfig.getAccessTokenExpiration()); // nhận vào giây, ở đây là 1h
        response.addCookie(accessCookie);

        // set refresh token
        Cookie refreshCookie = new Cookie("refresh_token", res.getRefreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/"); // chỉ gửi cookie khi call đúng endpoint này
        refreshCookie.setMaxAge((int) jwtConfig.getRefreshTokenExpiration()); // 1 ngày
        response.addCookie(refreshCookie);

        return new ResponseEntity<>(new BaseResponse<>("Đăng nhập thành công", res,
                200), HttpStatus.OK);

    }

    @GetMapping("/me")
    public UserEntity me(Authentication authentication) {

        Long userId = Long.parseLong(authentication.getName());
        return userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedError("user not found"));

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletRequest request,
            HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.ok("Logout successsful");
    }

    @PostMapping("/google")
    public ResponseEntity<?> loginGoogle(@AuthenticationPrincipal Jwt jwt, HttpServletResponse response) {
        try {
            AuthGoogleResponse res = authService.loginWithGoogle(jwt);
            Cookie accessCookie = new Cookie("access_token", res.getAccessToken());
            accessCookie.setHttpOnly(true);
            accessCookie.setPath("/");
            accessCookie.setMaxAge((int) jwtConfig.getAccessTokenExpiration());
            response.addCookie(accessCookie);

            // set refresh token
            Cookie refreshCookie = new Cookie("refresh_token", res.getRefreshToken());
            refreshCookie.setHttpOnly(true);
            refreshCookie.setPath("/"); // chỉ gửi cookie khi call đúng endpoint này
            refreshCookie.setMaxAge((int) jwtConfig.getRefreshTokenExpiration()); // 1 ngày
            response.addCookie(refreshCookie);
            return new ResponseEntity<>(new BaseResponse<>("Đăng nhập thành công", res, 200), HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new BaseResponse<>(e.getMessage(), 409));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            HttpServletRequest request,
            HttpServletResponse response) {

        String refreshToken = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        String newAccessToken = authService.refreshToken(refreshToken);
        Cookie accessCookie = new Cookie(
                "access_token",
                newAccessToken);

        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge((int) jwtConfig.getAccessTokenExpiration());

        response.addCookie(accessCookie);

        return ResponseEntity.ok(
                Map.of("message", "Refresh token success"));
    }
}
