package com.tracker.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record TopicRequest(
        @NotBlank(message = "Topic name cannot be blank")
        String name,

        @Min(value = 0, message = "Confidence must be >= 0")
        @Max(value = 100, message = "Confidence must be <= 100")
        int confidence,
        LocalDate deadline
) {
}
