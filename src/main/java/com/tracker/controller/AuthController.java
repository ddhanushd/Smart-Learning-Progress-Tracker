package com.tracker.controller;

import com.tracker.model.User;
import com.tracker.repository.UserRepository;
import com.tracker.security.JwtUtil;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

    //LOGIN → access + refresh token
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {

        String username = request.get("username");
        log.info("Login attempt received for user: {}", username);

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            request.get("password")
                    )
            );
        } catch (Exception ex) {
            log.warn("Authentication failed for user: {}", username);
            throw ex;
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("Authenticated user not found in DB: {}", username);
                    return new RuntimeException("User not found");
                });

        String accessToken = jwtUtil.generateToken(username);

        String refreshToken = UUID.randomUUID().toString();
        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpiry(
                Instant.now().plus(7, ChronoUnit.DAYS)
        );

        userRepository.save(user);

        log.info("Login successful for user: {}", username);

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "type", "Bearer"
        );
    }


    // REFRESH TOKEN → new access token
    @PostMapping("/refresh")
    public Map<String, String> refresh(@RequestBody Map<String, String> request) {

        log.info("Refresh token request received");

        String refreshToken = request.get("refreshToken");

        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> {
                    log.warn("Invalid refresh token used");
                    return new RuntimeException("Invalid refresh token");
                });

        if (user.getRefreshTokenExpiry() == null ||
                user.getRefreshTokenExpiry().isBefore(Instant.now())) {

            log.warn("Expired refresh token for user: {}", user.getUsername());
            throw new RuntimeException("Refresh token expired");
        }

        String newAccessToken = jwtUtil.generateToken(user.getUsername());

        log.info("Access token refreshed for user: {}", user.getUsername());

        return Map.of(
                "accessToken", newAccessToken,
                "type", "Bearer"
        );
    }


    @PostMapping("/logout")
    public Map<String, String> logout(@RequestBody Map<String, String> request) {

        log.info("Logout request received");

        String refreshToken = request.get("refreshToken");

        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> {
                    log.warn("Invalid refresh token during logout");
                    return new RuntimeException("Invalid refresh token");
                });

        user.setRefreshToken(null);
        user.setRefreshTokenExpiry(null);

        userRepository.save(user);

        log.info("User logged out successfully: {}", user.getUsername());

        return Map.of(
                "message", "Logged out successfully"
        );
    }


}
