package com.shuttletime.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class LoginResponse {
    private UUID userId;
    private String name;
    private String email;
    private String jwt;
    private String message;

    public LoginResponse(UUID userId, String name, String email, String jwt, String message) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.jwt = jwt;
        this.message = message;
    }

    public LoginResponse(String message, UUID userId, String name) {
        this.message = message;
        this.userId = userId;
        this.name = name;
    }

    public LoginResponse(String message, UUID userId, String username, String token) {
        this.message = message;
        this.userId = userId;
        this.name = username;
        this.jwt = token;
    }
}

