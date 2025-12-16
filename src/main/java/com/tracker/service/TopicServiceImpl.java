package com.tracker.service;

import com.tracker.dto.BulkUploadResponse;
import com.tracker.dto.LearningStats;
import com.tracker.dto.TopicRequest;
import com.tracker.model.*;
import com.tracker.repository.TopicRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    public TopicServiceImpl(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @Override
    public Topic createTopic(TopicRequest request) {

        log.info("Creating topic: {}", request.name());

        if (request.name().isBlank()) {
            log.warn("Attempted to create topic with blank name");
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

        Topic saved = topicRepository.save(topic);
        log.info("Topic created successfully with id: {}", saved.getId());

        return saved;
    }

    @Override
    public List<Topic> listAll() {
        log.info("Fetching all topics");
        return topicRepository.findAll();
    }

    @Override
    public List<Topic> getWeakTopics() {
        log.info("Fetching WEAK topics");
        return topicRepository.findByStatus("WEAK");
    }

    @Override
    public Topic updateConfidence(String id, int newConfidence, String note) {

        log.info("Revising topic with id: {}", id);

        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Topic not found for revision: {}", id);
                    return new RuntimeException("Topic not found");
                });

        int oldConfidence = topic.getConfidence();

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

        Topic updated = topicRepository.save(topic);

        log.info("Revision added for topic {} : {} -> {}", topic.getName(), oldConfidence, newConfidence);

        return updated;
    }

    @Override
    public void deleteTopic(String id) {
        log.warn("Deleting topic with id: {}", id);
        topicRepository.deleteById(id);
    }

    @Override
    public LearningStats getStats() {
        log.info("Calculating learning statistics");

        long total = topicRepository.count();
        long strong = topicRepository.findByStatus("STRONG").size();
        long average = topicRepository.findByStatus("AVERAGE").size();
        long weak = topicRepository.findByStatus("WEAK").size();

        return new LearningStats(total, strong, average, weak);
    }

    @Override
    public List<Topic> search(String keyword) {
        log.info("Searching topics with keyword: {}", keyword);
        return topicRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<Topic> getSortedByConfidence() {
        log.info("Fetching topics sorted by confidence");
        return topicRepository.findAllByOrderByConfidenceDesc();
    }

    @Override
    public BulkUploadResponse bulkUpload(List<TopicRequest> requests) {

        log.info("Bulk upload started with {} topics", requests.size());

        int success = 0;
        int failed = 0;

        for (TopicRequest request : requests) {
            try {
                createTopic(request);
                success++;
            } catch (Exception e) {
                log.error("Failed to upload topic: {}", request.name());
                failed++;
            }
        }

        log.info("Bulk upload completed. Success: {}, Failed: {}", success, failed);

        return new BulkUploadResponse(
                requests.size(),
                success,
                failed
        );
    }

    @Override
    public List<Topic> getOverdueTopics() {
        log.info("Fetching overdue topics");
        return topicRepository.findByDeadlineBeforeAndCompletedFalse(LocalDate.now());
    }

    @Override
    public Topic updateDeadline(String id, LocalDate deadline) {

        log.info("Updating deadline for topic: {}", id);

        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Topic not found while updating deadline: {}", id);
                    return new RuntimeException("Topic not found");
                });

        topic.setDeadline(deadline);

        return topicRepository.save(topic);
    }

    @Override
    public Page<Topic> getPagedTopics(int page, int size) {
        log.info("Fetching paged topics - page: {}, size: {}", page, size);
        return topicRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Topic markCompleted(String id) {

        log.info("Marking topic as completed: {}", id);

        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Topic not found while marking completed: {}", id);
                    return new RuntimeException("Topic not found");
                });

        topic.setCompleted(true);
        return topicRepository.save(topic);
    }

    @Override
    public Topic getById(String id) {
        log.info("Fetching topic by id: {}", id);

        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Topic not found with id: {}", id);
                    return new RuntimeException("Topic not found with id: " + id);
                });

        log.info("Topic fetched successfully: {}", topic.getName());

        return topic;
    }

    // ================= PRIVATE BUSINESS LOGIC =================

    private LearningStatus evaluateStatus(int confidence) {
        if (confidence >= 80) return new Strong();
        else if (confidence >= 50) return new Average();
        else return new Weak();
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
