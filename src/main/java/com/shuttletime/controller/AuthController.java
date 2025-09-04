package com.shuttletime.controller;

import com.shuttletime.model.dto.LoginRequest;
import com.shuttletime.model.dto.LoginResponse;
import com.shuttletime.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(
        origins = {
                "http://localhost:5173",              // Local React dev frontend
                "https://shuttle-ui.vercel.app"      // Vercel frontend
        },
        allowedHeaders = "*",
        allowCredentials = "true"
)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
