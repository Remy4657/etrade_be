package com.example.demo.dto.res;

import lombok.Data;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponse {

    private String username;
    private String email;
    private List<String> roles;

}
