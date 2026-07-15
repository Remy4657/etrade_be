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

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtil;

    private static final AntPathMatcher matcher = new AntPathMatcher();

    private static final List<String> EXCLUDE_URLS = List.of(
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/api/v1/auth/refresh",
            "/api/v1/auth/logout",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/uploads/**",
            "/api/v1/products/**",
            "/api/v1/product/**",
            "/api/v1/categories/**");

    @Override
    // cac url trong shouldNotFilter thi doFilterInternal() KHÔNG được gọi
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();

        // nếu requestURI khớp với bất kỳ pattern nào trong EXCLUDE_URLS thì trả về true
        // → KHÔNG gọi doFilterInternal() để check JWT, ngược lại trả về false để gọi
        // doFilterInternal() và check JWT
        return EXCLUDE_URLS.stream()
                .anyMatch(pattern -> matcher.match(pattern, requestURI));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        // Nếu SecurityContext đã có auth (do filter trước set vào) → không cần check
        // nữa
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response); // cho request đi tiếp
            return;
        }

        try {
            // 2. Lấy accessToken từ COOKIE
            // String accessToken = getAccessToken(request);
            String refreshToken = jwtUtil.getToken(request, "refresh_token");
            String accessToken = jwtUtil.getToken(request, "access_token");

            System.out.println(
                    request.getMethod() + " " + request.getRequestURI() +
                            "==access_token: " + accessToken);
            System.out.println(
                    request.getMethod() + " " + request.getRequestURI() +
                            "==refresh_token: " + refreshToken);

            // Không có accessToken → cho request đi tiếp (controller sẽ bị chặn nếu cần
            // auth được cấu hình ở config)
            if (refreshToken == null) {
                // filterChain.doFilter(request, response);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                String json = "{\"code\":401, \"error\":\"Unauthorized\", \"message\":\"Token không hợp lệ hoặc hết hạn\"}";

                response.getWriter().write(json);
                response.getWriter().flush();
                return;
            }

            if (accessToken == null) {
                // filterChain.doFilter(request, response);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                String json = "{\"code\":403, \"error\":\"Unauthorized\", \"message\":\"Bạn không có có quyền cho chức năng này\"}";

                response.getWriter().write(json);
                response.getWriter().flush();
                return;
            }
            // 2.5 CHECK TOKEN CÓ BỊ REVOKE KHÔNG
            // if (tokenBlacklistRepository.existsByToken(accessToken)) {
            // throw new RuntimeException("Token revoked");
            // }
            // 3. Validate accessToken (expire, signature, etc.)
            // if (jwtUtil.isTokenExpired(accessToken)) { // invalid accessToken
            // filterChain.doFilter(request, response);
            // return;
            // }
            // 4. Giải mã accessToken → lấy userId
            Long userId = Long.parseLong(jwtUtil.getUserId(accessToken)); // nếu accessToken không hợp lệ thì chỗ này
                                                                          // lỗi sẽ rơi vào catch

            System.out.println("userIdContext " + userId);
            if (userId != null) {
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
