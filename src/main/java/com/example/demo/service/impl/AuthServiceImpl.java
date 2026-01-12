package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.example.demo.dto.req.LoginRequest;
import com.example.demo.dto.req.RegisterRequest;
import com.example.demo.dto.res.AuthGoogleResponse;
import com.example.demo.dto.res.AuthResponse;
import com.example.demo.entity.user.RoleEntity;
import com.example.demo.entity.user.TokenBlacklistEntity;
import com.example.demo.entity.user.UserEntity;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.TokenBlacklistRepository;
import com.example.demo.repository.UserRepository;

import com.example.demo.security.JwtUtil;
import com.example.demo.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    private void assignDefaultRole(UserEntity user) {
        RoleEntity userRole = roleRepository.findByName("user");
        user.getRoleList().add(userRole);
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException("Email already exists");
        }
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        assignDefaultRole(user);

        userRepository.save(user);
        List<String> roles = user.getRoleList()
                .stream()
                .map(RoleEntity::getName)
                .toList();
        String token = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getEmail(), roles);

        return new AuthResponse(
                token,
                user.getUsername(),
                user.getEmail(),
                roles);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }
        List<String> roles = user.getRoleList()
                .stream()
                .map(RoleEntity::getName)
                .toList();
        String token = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getEmail(), roles);
        return new AuthResponse(
                token,
                user.getUsername(),
                user.getEmail(),
                roles);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {

        // 1. Lấy token từ cookie
        String token = extractTokenFromCookie(request);

        // 2. Revoke token (nếu tồn tại)
        if (token != null && !tokenBlacklistRepository.existsByToken(token)) {
            TokenBlacklistEntity blacklist = new TokenBlacklistEntity();
            blacklist.setToken(token);
            blacklist.setExpiredAt(jwtUtil.getExpiration(token));
            tokenBlacklistRepository.save(blacklist);
        }

        // 3. Xóa cookie access_token
        clearCookie(response, "access_token");
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null)
            return null;

        for (Cookie cookie : request.getCookies()) {
            if ("access_token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private void clearCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // nếu dùng https
        cookie.setPath("/");
        cookie.setMaxAge(0); // xóa cookie

        response.addCookie(cookie);
    }

    // clear blacklist token
    @Override
    @Scheduled(cron = "0 0 * * * *") // mỗi phút
    @Transactional
    public void cleanExpiredTokens() {
        int count = tokenBlacklistRepository.deleteByExpiredAtBefore(LocalDateTime.now());
        System.out.println("Deleted expired tokens: " + count);
    }

    @Override
    @Transactional
    public AuthGoogleResponse loginWithGoogle(Jwt jwt) {

        String email = jwt.getClaim("email");
        String name = jwt.getClaim("name");
        String googleSub = jwt.getSubject();

        UserEntity user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    UserEntity newUser = new UserEntity();
                    newUser.setEmail(email);
                    newUser.setUsername(name);
                    newUser.setProvider("GOOGLE");
                    newUser.setProviderId(googleSub);
                    if (newUser.getRoleList().isEmpty()) {
                        assignDefaultRole(newUser);
                    }
                    return userRepository.save(newUser);
                });
        List<String> roles = user.getRoleList()
                .stream()
                .map(RoleEntity::getName)
                .toList();
        return AuthGoogleResponse.builder()
                .accessToken(jwtUtil.generateToken(user.getUsername(), user.getId(), user.getEmail(), roles))
                .roles(roles)
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }
}
