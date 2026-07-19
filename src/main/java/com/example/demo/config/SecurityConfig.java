package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.config.Customizer.withDefaults;

import java.util.List;

import com.example.demo.security.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
        @Autowired
        private final JwtFilter jwtFilter;

        // cấu hình cors
        @Bean
        public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
                org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();

                config.setAllowedOriginPatterns(List.of(
                                "http://localhost:*",
                                "http://192.168.*.*:*",
                                "https://megadeal.dev",
                                "https://www.megadeal.dev"));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                config.setAllowedHeaders(List.of("*"));
                config.setAllowCredentials(true);

                org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", config);

                return source;
        }

        /**
         * CHAIN 1: Verify GOOGLE_ID_TOKEN
         * /api/v1/auth/google
         * ↓
         * googleLoginChain
         * ↓
         * Spring OAuth2 JWT filter
         * ↓
         * verify GOOGLE TOKEN
         * ↓
         * authenticated()
         * ↓
         * controller
         *
         * Spring tự verify Google ID Token bằng cách cấu hình
         * .oauth2ResourceServer().jwt()
         * Nếu token hợp lệ, Spring sẽ tạo Authentication object và lưu vào
         * SecurityContext
         * Không có .addFilterBefore(jwtFilter, ...) vì Google ID Token đã được Spring
         * verify
         * (nên JwtFilter không chạy.)
         *
         */
        @Bean
        @Order(1)
        public SecurityFilterChain googleLoginChain(HttpSecurity http) throws Exception {
                http
                                .securityMatcher("/api/v1/auth/google")
                                .csrf(csrf -> csrf.disable())
                                .cors(withDefaults())
                                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                                // Spring tự verify Google ID Token
                                .oauth2ResourceServer(oauth2 -> oauth2
                                                .jwt(withDefaults()));

                return http.build();
        }

        /**
         * CHAIN 2: Flow request thường
         * Ví dụ:
         * GET /api/v1/users/me
         * Cookie: access_token=abc
         *
         * /api/v1/users/me
         * ↓
         * apiChain
         * ↓
         * jwtFilter
         * ↓
         * verify BACKEND JWT
         * ↓
         * authenticated()
         * ↓
         * controller
         */
        @Bean
        @Order(2)
        public SecurityFilterChain apiChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .cors(withDefaults())
                                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/api/v1/products/**",
                                                                "/api/v1/product/**",
                                                                "/api/v1/categories/**",
                                                                "/api/v1/auth/login",
                                                                "/api/v1/auth/logout",
                                                                "/api/v1/auth/register",
                                                                "/api/v1/auth/refresh",
                                                                "/swagger-ui/**",
                                                                "/v3/api-docs/**")
                                                .permitAll() // cho phép truy cập không cần auth
                                                .anyRequest().authenticated()) // các request khác cần auth
                                .httpBasic(AbstractHttpConfigurer::disable)
                                .formLogin(AbstractHttpConfigurer::disable)
                                // JWT BACKEND FILTER
                                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
