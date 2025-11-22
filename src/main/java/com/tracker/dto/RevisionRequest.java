package com.tracker.dto;

public record RevisionRequest(
        int confidence,
        String note
) {
}
