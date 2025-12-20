package com.tracker.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        ApiResponse<Object> apiResponse =
                new ApiResponse<>(false, "Unauthorized: Invalid or missing token", null);

        new ObjectMapper().writeValue(
                response.getOutputStream(),
                apiResponse
        );
    }
}
