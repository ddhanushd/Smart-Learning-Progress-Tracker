package com.tracker.dto;

import java.time.LocalDate;
import java.util.List;

public record TopicResponse(
        String id,
        String name,
        int confidence,
        String status,
        String suggestion,
        boolean completed,
        LocalDate deadline,
        List<RevisionResponse> revisions
) {
}
