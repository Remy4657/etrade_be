package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
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
        private final JwtFilter jwtFilter;

        /**
         * CHAIN 1: CHỈ cho Google Login
         * Verify GOOGLE_ID_TOKEN
         */
        @Bean
        @Order(1)
        public SecurityFilterChain googleLoginChain(HttpSecurity http) throws Exception {
                http
                                .securityMatcher("/api/v1/auth/google")
                                .csrf(csrf -> csrf.disable())
                                .cors(cors -> {
                                })
                                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                                // Spring tự verify Google ID Token
                                .oauth2ResourceServer(oauth2 -> oauth2
                                                .jwt(withDefaults()));

                return http.build();
        }

        /**
         * CHAIN 2: API + login user/pass
         * Verify JWT BACKEND
         */
        @Bean
        @Order(2)
        public SecurityFilterChain apiChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .cors(cors -> {
                                })
                                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/api/v1/product/**",
                                                                "/api/v1/products/**",
                                                                "/api/v1/categories/**",
                                                                "/api/v1/cart/**",
                                                                "/api/v1/auth/login",
                                                                "/api/v1/auth/register")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .httpBasic(AbstractHttpConfigurer::disable)
                                .formLogin(AbstractHttpConfigurer::disable)

                                // ⬇️ JWT BACKEND FILTER
                                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        // @Bean
        // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // http
        // .csrf(csrf -> csrf.disable())
        // .cors(cors -> {
        // })
        // .authorizeHttpRequests(auth -> auth
        // .requestMatchers(
        // "/api/v1/product/**",
        // "/api/v1/products/**",
        // "/api/v1/categories/**",
        // "/api/v1/cart/**",
        // "/api/v1/auth/login",
        // "/api/v1/auth/register")
        // .permitAll()
        // .anyRequest().authenticated())
        // // không dùng form login / basic
        // .httpBasic(AbstractHttpConfigurer::disable)
        // .formLogin(AbstractHttpConfigurer::disable)
        // .logout(logout -> {
        // })
        // // VERIFY GOOGLE ID_TOKEN
        // // .oauth2ResourceServer(oauth2 -> oauth2
        // // .jwt(withDefaults()));
        // // .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt
        // // .jwtAuthenticationConverter(jwtAuthenticationConverter())));
        // // JWT BACKEND (sau OAuth2)
        // .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // return http.build();
        // }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
