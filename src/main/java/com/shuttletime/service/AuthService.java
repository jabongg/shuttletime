package com.shuttletime.service;

import com.shuttletime.model.dto.LoginRequest;
import com.shuttletime.model.dto.LoginResponse;
import com.shuttletime.model.entity.User;
import com.shuttletime.repository.UserRepository;
import com.shuttletime.util.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with email: " + request.getEmail());
        }

        User user = userOpt.get();
        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // 🔑 Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail());

        return new LoginResponse(
                "Login successful",
                user.getUserId(),
                user.getUsername(),
                token  // ✅ Now response carries the JWT
        );
    }
}
