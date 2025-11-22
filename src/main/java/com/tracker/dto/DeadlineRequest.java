package com.tracker.dto;

import java.time.LocalDate;

public record DeadlineRequest(
        LocalDate deadline
) {
}
