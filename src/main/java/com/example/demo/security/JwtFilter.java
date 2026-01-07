package com.example.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.repository.TokenBlacklistRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private static final AntPathMatcher matcher = new AntPathMatcher();

    private static final List<String> EXCLUDE_URLS = List.of(
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/uploads/**");

    @Override
    // cac url trong shouldNotFilter thi doFilterInternal() KHÔNG được gọi
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return EXCLUDE_URLS.stream()
                .anyMatch(pattern -> matcher.match(pattern, path));
    }

    private String extractToken(HttpServletRequest request) {
        if (request.getCookies() == null)
            return null;

        for (Cookie cookie : request.getCookies()) {
            if ("access_token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        // 1. Nếu đã có authentication rồi thì bỏ qua
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }
        // 2. Lấy token từ COOKIE
        String token = extractToken(request);
        System.out.println(
                request.getMethod() + " " + request.getRequestURI() +
                        "==access_token: " + token);
        // Không có token → cho request đi tiếp (controller sẽ bị chặn nếu cần auth được
        // cấu hình ở config)
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 2.5 CHECK TOKEN CÓ BỊ REVOKE KHÔNG
            if (tokenBlacklistRepository.existsByToken(token)) {
                throw new RuntimeException("Token revoked");
            }
            // 3. Validate token (expire, signature, etc.)
            if (!jwtUtil.validateToken(token)) {
                filterChain.doFilter(request, response);
                return;
            }
            // 4. Giải mã token → lấy userId
            Long userId = Long.parseLong(jwtUtil.getUserId(token));
            System.out.println("userId1: " + userId);

            if (userId != null) {
                System.out.println("userId2: " + userId);
                // 5. Tạo Authentication
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        List.of() // hoặc authorities nếu có
                );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request));

                // 6. Set vào SecurityContext
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }

        } catch (Exception e) {
            // Token sai / hết hạn → không set auth
            System.out.println("JWT Filter error: " + e.getMessage());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
