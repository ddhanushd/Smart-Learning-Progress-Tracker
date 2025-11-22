package com.tracker.dto;

public record BulkUploadResponse(
        int totalUploaded,
        int successful,
        int failed
) {
}
