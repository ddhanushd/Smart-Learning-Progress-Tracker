package com.tracker.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;


@Configuration
public class GlobalCorsFilter {

    @Bean
    public Filter corsFilter() {
        return (ServletRequest request, ServletResponse response, FilterChain chain) -> {

            HttpServletResponse res = (HttpServletResponse) response;

            res.setHeader("Access-Control-Allow-Origin",
                    "https://smart-learning-progress-tracker-ui.vercel.app");
            res.setHeader("Access-Control-Allow-Methods",
                    "GET,POST,PUT,DELETE,OPTIONS");
            res.setHeader("Access-Control-Allow-Headers",
                    "Authorization,Content-Type");
            res.setHeader("Access-Control-Allow-Credentials", "true");

            chain.doFilter(request, response);
        };
    }
}
