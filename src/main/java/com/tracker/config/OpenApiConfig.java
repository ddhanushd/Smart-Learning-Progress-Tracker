package com.tracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI learningTrackerApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Smart Learning Tracker API")
                        .description("Backend API for tracking learning progress, revisions, deadlines and analytics")
                        .version("v1.0"));
    }
}
