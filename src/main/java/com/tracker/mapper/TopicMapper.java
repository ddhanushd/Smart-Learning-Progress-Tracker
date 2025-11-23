package com.tracker.mapper;

import com.tracker.dto.RevisionResponse;
import com.tracker.dto.TopicResponse;
import com.tracker.model.Revision;
import com.tracker.model.Topic;

import java.util.List;

public class TopicMapper {
    public static TopicResponse toResponse(Topic topic) {
        return new TopicResponse(
                topic.getId(),
                topic.getName(),
                topic.getConfidence(),
                topic.getStatus(),
                topic.getSuggestion(),
                topic.isCompleted(),
                topic.getDeadline(),
                mapRevisions(topic.getRevisions())
        );
    }

    private static List<RevisionResponse> mapRevisions(List<Revision> revisions) {
        return revisions.stream()
                .map(r -> new RevisionResponse(
                        r.getRevisedAt(),
                        r.getOldConfidence(),
                        r.getNewConfidence(),
                        r.getNote()
                ))
                .toList();
    }
}
