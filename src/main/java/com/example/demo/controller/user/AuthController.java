package com.example.demo.controller.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.req.LoginRequest;
import com.example.demo.dto.req.RegisterRequest;
import com.example.demo.dto.res.ApiResponse;
import com.example.demo.dto.res.AuthResponse;
import com.example.demo.dto.res.BaseResponse;
import com.example.demo.entity.user.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

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

    @GetMapping("/me")
    // public Map<String, Object> me() {
    // Map<String, Object> res = new HashMap<>();
    // res.put("id", 1);
    // res.put("username", "test_user");
    // res.put("email", "test@gmail.com");
    // res.put("role", "USER");
    // return res;
    // }
    public UserEntity me(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

}
