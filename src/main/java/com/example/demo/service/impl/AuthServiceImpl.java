package com.example.demo.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.req.LoginRequest;
import com.example.demo.dto.req.RegisterRequest;
import com.example.demo.dto.res.AuthResponse;
import com.example.demo.entity.RoleEntity;
import com.example.demo.entity.user.UserEntity;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException("Email already exists");
        }
        RoleEntity userRole = roleRepository.findByName("user");
        // .orElseThrow(() -> new RuntimeException("Role USER not found"));

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.getRoleList().add(userRole);

        userRepository.save(user);
        List<String> roles = user.getRoleList()
                .stream()
                .map(RoleEntity::getName)
                .toList();
        String token = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getEmail(), roles);

        return new AuthResponse(
                token,
                user.getUsername(),
                user.getEmail());
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
                user.getEmail());
    }
}
