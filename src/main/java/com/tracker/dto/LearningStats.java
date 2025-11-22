package com.tracker.dto;

public record LearningStats(
        long totalTopics,
        long strongCount,
        long averageCount,
        long weakCount
) {
}
