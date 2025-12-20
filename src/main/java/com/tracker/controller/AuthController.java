package com.tracker.controller;

import com.tracker.model.User;
import com.tracker.repository.UserRepository;
import com.tracker.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    // 🔐 LOGIN → access + refresh token
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.get("username"),
                                request.get("password")
                        )
                );

        String username = request.get("username");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Access token (JWT)
        String accessToken = jwtUtil.generateToken(username);

        // Refresh token (UUID)
        String refreshToken = UUID.randomUUID().toString();
        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpiry(
                Instant.now().plus(7, ChronoUnit.DAYS)
        );

        userRepository.save(user);
        System.out.println("LOGIN refreshToken = " + refreshToken);
        System.out.println("LOGIN expiry = " + user.getRefreshTokenExpiry());

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "type", "Bearer"
        );
    }

    // 🔁 REFRESH TOKEN → new access token
    @PostMapping("/refresh")
    public Map<String, String> refresh(@RequestBody Map<String, String> request) {

        String refreshToken = request.get("refreshToken");

        System.out.println("REFRESH token received = " + refreshToken);

        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        System.out.println("DB token = " + user.getRefreshToken());
        System.out.println("DB expiry = " + user.getRefreshTokenExpiry());

        // ✅ NULL SAFETY CHECK
        if (user.getRefreshTokenExpiry() == null ||
                user.getRefreshTokenExpiry().isBefore(Instant.now())) {

            throw new RuntimeException("Refresh token expired or invalid");
        }

        String newAccessToken =
                jwtUtil.generateToken(user.getUsername());

        return Map.of(
                "accessToken", newAccessToken,
                "type", "Bearer"
        );
    }

    @PostMapping("/logout")
    public Map<String, String> logout(@RequestBody Map<String, String> request) {

        String refreshToken = request.get("refreshToken");

        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        // 🔒 Invalidate refresh token
        user.setRefreshToken(null);
        user.setRefreshTokenExpiry(null);

        userRepository.save(user);

        return Map.of(
                "message", "Logged out successfully"
        );
    }

}
