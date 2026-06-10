package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.example.demo.dto.req.LoginRequest;
import com.example.demo.dto.req.RegisterRequest;
import com.example.demo.dto.res.AuthGoogleResponse;
import com.example.demo.dto.res.AuthResponse;
import com.example.demo.dto.res.SignupResponse;
import com.example.demo.entity.user.RefreshTokenEntity;
import com.example.demo.entity.user.RoleEntity;
import com.example.demo.entity.user.UserEntity;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.repository.RoleRepository;
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
    private final RefreshTokenRepository refreshTokenRepository;

    private void assignDefaultRole(UserEntity user) {
        RoleEntity userRole = roleRepository.findByName("user");
        user.getRoleList().add(userRole);
    }

    // clear blacklist token
    // @Override
    // @Scheduled(cron = "0 0 * * * *") // mỗi phút
    // @Transactional
    // public void cleanExpiredTokens() {
    // int count =
    // tokenBlacklistRepository.deleteByExpiredAtBefore(LocalDateTime.now());
    // System.out.println("Deleted expired tokens: " + count);
    // }

    @Override
    public SignupResponse register(RegisterRequest request) {
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

        return new SignupResponse(
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

        String accessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getId(), user.getEmail(), roles);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        RefreshTokenEntity tokenEntity = RefreshTokenEntity.builder()
                .token(refreshToken)
                .expiredAt(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .user(user)
                .build();

        refreshTokenRepository.save(tokenEntity);

        return new AuthResponse(
                accessToken,
                refreshToken,
                user.getUsername(),
                user.getEmail(),
                roles);
    }

    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {

        // 1. Lấy refresh token từ cookie
        String refreshToken = extractTokenFromCookie(request, "refresh_token");

        if (refreshToken != null && !refreshToken.isBlank()) {
            try {
                // Xóa refresh token khỏi DB (nếu tồn tại)
                int deletedCount = refreshTokenRepository.deleteByToken(refreshToken);
                if (deletedCount > 0) {
                    System.out.println("Refresh token deleted: {}" + refreshToken);
                } else {
                    System.out.println("Refresh token not found, might be already removed");
                }
            } catch (Exception e) {
                System.out.println("Error deleting refresh token from DB: {}" + e.getMessage());
            }
        }

        // 2. (Tùy chọn) Blacklist access token nếu bạn muốn ngăn dùng lại access token
        // trước khi hết hạn
        // String accessToken = extractTokenFromCookie(request, "access_token");
        // if (accessToken != null &&
        // !tokenBlacklistRepository.existsByToken(accessToken)) {
        // TokenBlacklistEntity blacklist = new TokenBlacklistEntity();
        // blacklist.setToken(accessToken);
        // blacklist.setExpiredAt(jwtUtil.getExpirationDate(accessToken));
        // tokenBlacklistRepository.save(blacklist);
        // }

        // 3. Xóa cookie access_token và refresh_token
        clearCookie(response, "access_token");
        clearCookie(response, "refresh_token");

    }

    private String extractTokenFromCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null)
            return null;
        return Arrays.stream(request.getCookies())
                .filter(c -> cookieName.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    private void clearCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        // cookie.setSecure(true); // nếu dùng https
        cookie.setPath("/");
        cookie.setMaxAge(0); // xóa cookie

        response.addCookie(cookie);
    }

    @Override
    @Transactional
    public AuthGoogleResponse loginWithGoogle(Jwt jwt) {
        try {
            String email = jwt.getClaim("email");
            String name = jwt.getClaim("name");

            String googleSub = jwt.getSubject(); // sub là định danh duy nhất của user trên Google, có thể dùng để lưu
                                                 // vào db nếu muốn

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

            String accessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getId(), user.getEmail(), roles);
            String refreshToken = jwtUtil.generateRefreshToken(user);

            RefreshTokenEntity tokenEntity = RefreshTokenEntity.builder()
                    .token(refreshToken)
                    .expiredAt(LocalDateTime.now().plusDays(7))
                    .revoked(false)
                    .user(user)
                    .build();

            refreshTokenRepository.save(tokenEntity);

            return AuthGoogleResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .roles(roles)
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .build();

        } catch (Exception e) {
            System.err.println("GOOGLE LOGIN ERROR");
            e.printStackTrace(); // CÁI QUAN TRỌNG NHẤT
            throw e; // bắt buộc throw lại để NextAuth biết là fail
        }
    }

    @Override
    public String refreshToken(String refreshToken) {

        if (refreshToken == null) {
            throw new RuntimeException("Refresh token missing");
        }
        RefreshTokenEntity tokenEntity = refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token invalid"));

        if (tokenEntity.isRevoked()) {
            throw new RuntimeException("Refresh token revoked");
        }

        if (jwtUtil.isTokenExpired(refreshToken)) { // token hết hạn thì xóa luôn trong db
            refreshTokenRepository.delete(tokenEntity);

            throw new RuntimeException(
                    "Refresh token expired");
        }

        UserEntity user = tokenEntity.getUser();

        List<String> roles = user.getRoleList()
                .stream()
                .map(role -> role.getName())
                .toList();

        return jwtUtil.generateAccessToken(
                user.getUsername(),
                user.getId(),
                user.getEmail(),
                roles);

    }
}
