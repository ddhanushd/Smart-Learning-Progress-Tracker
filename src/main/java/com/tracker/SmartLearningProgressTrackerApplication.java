package com.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class SmartLearningProgressTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartLearningProgressTrackerApplication.class, args);
	}

}
