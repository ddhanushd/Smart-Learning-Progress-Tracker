package com.tracker.dto;

import java.time.LocalDateTime;

public record RevisionResponse(
        LocalDateTime revisedAt,
        int oldConfidence,
        int newConfidence,
        String note
) {
}
