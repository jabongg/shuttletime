package com.shuttletime.model.dto;

public class LoginResponse {
    private String message;
    private String userId;
    private String name;

    public LoginResponse(String message, String userId, String name) {
        this.message = message;
        this.userId = userId;
        this.name = name;
    }

    public String getMessage() {
        return message;
    }
    public String getUserId() {
        return userId;
    }
    public String getName() {
        return name;
    }
}