package com.example.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    private static final AntPathMatcher matcher = new AntPathMatcher();

    private static final List<String> EXCLUDE_URLS = List.of(
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            // "/api/v1/orders/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/uploads/**");

    @Override
    // doFilterInternal() KHÔNG được gọi
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
        // Nếu đã có authentication rồi thì bỏ qua
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }
        // 1. Lấy token từ COOKIE
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
            // Validate token (expire, signature, etc.)
            // if (!jwtUtil.validateToken(token)) {
            // throw new RuntimeException("Invalid token");
            // }
            // 2. Giải mã token → lấy username
            String idStr = jwtUtil.getUserId(token);
            Long userId = Long.parseLong(idStr);
            System.out.println("userId1: " + userId);
            if (userId != null) {
                System.out.println("userId2: " + userId);
                // 4. Tạo Authentication
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        List.of() // hoặc authorities nếu có
                );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request));

                // 5. Set vào SecurityContext
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
            // else {
            // // Token hết hạn hoặc invalid
            // response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired or
            // invalid");
            // return; // dừng filter chain
            // }

        } catch (Exception e) {
            // Token sai / hết hạn → không set auth
            System.out.println("JWT Filter error: " + e.getMessage());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
