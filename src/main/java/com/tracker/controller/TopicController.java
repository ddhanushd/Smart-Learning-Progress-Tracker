package com.tracker.controller;

import com.tracker.dto.*;
import com.tracker.model.Topic;
import com.tracker.repository.TopicRepository;
import com.tracker.service.TopicService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    private final TopicService topicService;

    private final TopicRepository topicRepository;

    public TopicController(TopicService topicService, TopicRepository topicRepository) {
        this.topicService = topicService;
        this.topicRepository = topicRepository;
    }

    @GetMapping("/health")
    public String health() {
        return "Learning Tracker Backend is running ✅";
    }


    @PostMapping
    public ResponseEntity<Topic> create(@Valid @RequestBody TopicRequest request) {
        Topic saved = topicService.createTopic(request);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Topic>> getAll() {
        return ResponseEntity.ok(topicService.listAll());
    }

    @GetMapping("/weak")
    public ResponseEntity<List<Topic>> getWeakTopics() {
        return ResponseEntity.ok(topicService.getWeakTopics());
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Topic> updateConfidence(
//            @PathVariable String id,
//            @RequestParam int confidence
//    ) {
//        return ResponseEntity.ok(topicService.updateConfidence(id, confidence));
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTopic(@PathVariable String id) {
        topicService.deleteTopic(id);
        return ResponseEntity.ok("Topic deleted successfully");
    }

    @GetMapping("/stats")
    public ResponseEntity<LearningStats> stats() {
        return ResponseEntity.ok(topicService.getStats());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Topic>> search(@RequestParam String q) {
        return ResponseEntity.ok(topicService.search(q));
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<Topic>> sorted() {
        return ResponseEntity.ok(topicService.getSortedByConfidence());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Topic>> paged(
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<Topic> result = topicRepository.findAll(PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }

    @PostMapping("/bulk")
    public ResponseEntity<BulkUploadResponse> bulkUpload(
            @RequestBody List<TopicRequest> requests
    ) {
        return ResponseEntity.ok(topicService.bulkUpload(requests));
    }

    @PutMapping("/{id}/revise")
    public ResponseEntity<Topic> revise(
            @PathVariable String id,
            @RequestBody RevisionRequest request
    ) {
        return ResponseEntity.ok(
                topicService.updateConfidence(
                        id,
                        request.confidence(),
                        request.note()
                )
        );
    }


    @PutMapping("/{id}/complete")
    public ResponseEntity<Topic> markCompleted(@PathVariable String id) {

        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Topic not found"));

        topic.setCompleted(true);
        return ResponseEntity.ok(topicRepository.save(topic));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<Topic>> overdue() {
        return ResponseEntity.ok(topicService.getOverdueTopics());
    }

    @PutMapping("/{id}/deadline")
    public ResponseEntity<Topic> updateDeadline(
            @PathVariable String id,
            @RequestBody DeadlineRequest request
    ) {
        return ResponseEntity.ok(
                topicService.updateDeadline(id, request.deadline())
        );
    }



}
