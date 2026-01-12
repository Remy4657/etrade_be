package com.example.demo.dto.res;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthGoogleResponse {
    private String accessToken;
    private List<String> roles;
    private String email;
    private String username;
}
