package com.shuttletime.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.shuttletime.model.dto.LoginRequest;
import com.shuttletime.model.dto.LoginResponse;
import com.shuttletime.model.entity.User;
import com.shuttletime.repository.UserRepository;
import com.shuttletime.service.AuthService;
import com.shuttletime.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(
        origins = {
                "http://localhost:5173",              // Local React dev frontend
                "https://shuttle-ui.vercel.app"       // Vercel frontend
        },
        allowedHeaders = "*",
        allowCredentials = "true"
)
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, UserRepository userRepository, JwtUtil jwtUtil) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    // ---------------- Local login ----------------
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // ---------------- Google login ----------------
    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> payload) {
        String idTokenString = payload.get("token");

        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance()
            )
                    .setAudience(Collections.singletonList("213622836179-i9suleorjuh54qp9enfqm6q7vc8rbi0d.apps.googleusercontent.com")) // TODO replace
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken != null) {
                GoogleIdToken.Payload userPayload = idToken.getPayload();
                String email = userPayload.getEmail();
                String name = (String) userPayload.get("name");

                // 🔑 Check if user exists, else create
                Optional<User> existingUser = userRepository.findByEmail(email);
                User user = existingUser.orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setUsername(name);
                    // No password required for Google accounts
                    return userRepository.save(newUser);
                });

                // 🔑 Generate JWT
                String token = jwtUtil.generateToken(email);

                return ResponseEntity.ok(new LoginResponse(
                        user.getUserId(),
                        user.getUsername(),
                        user.getEmail(),
                        token,
                        "Google login successful!"
                ));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid ID token.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login error: " + e.getMessage());
        }
    }
}
