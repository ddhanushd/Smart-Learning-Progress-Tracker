package com.tracker.service;

import com.tracker.dto.BulkUploadResponse;
import com.tracker.dto.LearningStats;
import com.tracker.dto.TopicRequest;
import com.tracker.model.*;
import com.tracker.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private final TopicRepository topicRepository;

    public TopicServiceImpl(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @Override
    public Topic createTopic(TopicRequest request) {

        if (request.name().isBlank()) {
            throw new IllegalArgumentException("Topic name cannot be blank");
        }

        LearningStatus status = evaluateStatus(request.confidence());
        String suggestion = suggestionFor(status, request.name(), request.confidence());

        Topic topic = new Topic(
                request.name().strip(),
                request.confidence(),
                statusToString(status),
                suggestion
        );

        topic.setDeadline(request.deadline());
        topic.setCompleted(false);

        return topicRepository.save(topic);
    }

    @Override
    public List<Topic> listAll() {
        return topicRepository.findAll();
    }

    @Override
    public List<Topic> getWeakTopics() {
        return topicRepository.findByStatus("WEAK");
    }

    @Override
    public Topic updateConfidence(String id, int newConfidence, String note) {

        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Topic not found"));

        int oldConfidence = topic.getConfidence();

        // Add revision record
        Revision revision = new Revision(
                LocalDateTime.now(),
                oldConfidence,
                newConfidence,
                note
        );

        topic.getRevisions().add(revision);

        LearningStatus status = evaluateStatus(newConfidence);
        String suggestion = suggestionFor(status, topic.getName(), newConfidence);

        topic.setConfidence(newConfidence);
        topic.setStatus(statusToString(status));
        topic.setSuggestion(suggestion);

        return topicRepository.save(topic);
    }

    @Override
    public void deleteTopic(String id) {
        topicRepository.deleteById(id);
    }

    @Override
    public LearningStats getStats() {
        long total = topicRepository.count();
        long strong = topicRepository.findByStatus("STRONG").size();
        long average = topicRepository.findByStatus("AVERAGE").size();
        long weak = topicRepository.findByStatus("WEAK").size();

        return new LearningStats(total, strong, average, weak);
    }

    @Override
    public List<Topic> search(String keyword) {
        return topicRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<Topic> getSortedByConfidence() {
        return topicRepository.findAllByOrderByConfidenceDesc();
    }

    @Override
    public BulkUploadResponse bulkUpload(List<TopicRequest> requests) {
        int success = 0;
        int failed = 0;

        for (TopicRequest request : requests) {
            try {
                createTopic(request);  // reuse existing logic ✅
                success++;
            } catch (Exception e) {
                failed++;
            }
        }

        return new BulkUploadResponse(
                requests.size(),
                success,
                failed
        );
    }

    @Override
    public List<Topic> getOverdueTopics() {
        return topicRepository.findByDeadlineBeforeAndCompletedFalse(LocalDate.now());
    }

    @Override
    public Topic updateDeadline(String id, LocalDate deadline) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Topic not found"));

        topic.setDeadline(deadline);

        return topicRepository.save(topic);
    }

    // ---------------- PRIVATE BUSINESS LOGIC ----------------

    private LearningStatus evaluateStatus(int confidence) {
        if (confidence >= 80) {
            return new Strong();
        } else if (confidence >= 50) {
            return new Average();
        } else {
            return new Weak();
        }
    }

    private String statusToString(LearningStatus status) {
        return switch (status) {
            case Strong s -> "STRONG";
            case Average a -> "AVERAGE";
            case Weak w -> "WEAK";
        };
    }

    private String suggestionFor(LearningStatus status, String topicName, int confidence) {
        return switch (status) {
            case Strong s -> """
                    Topic: %s
                    Confidence: %d%%
                    Status: STRONG
                    Suggestion: You can move to the next topic, just revise once in a while.
                    """.formatted(topicName, confidence);

            case Average a -> """
                    Topic: %s
                    Confidence: %d%%
                    Status: AVERAGE
                    Suggestion: Revise this topic once more before going to the next.
                    """.formatted(topicName, confidence);

            case Weak w -> """
                    Topic: %s
                    Confidence: %d%%
                    Status: WEAK
                    Suggestion: Revise this topic 2-3 times and practice small programs.
                    """.formatted(topicName, confidence);
        };
    }
}
