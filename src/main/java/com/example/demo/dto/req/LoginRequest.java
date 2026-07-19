package com.example.demo.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginRequest {
    @Schema(description = "Email", example = "test2@gmail.com")
    private String email;
    @Schema(description = "Password", example = "123456")
    private String password;
}
