package com.tracker.controller;

import com.tracker.dto.*;
import com.tracker.mapper.TopicMapper;
import com.tracker.service.TopicService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/topics")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Service status", "Learning Tracker Backend is running ✅")
        );
    }

    // ================= CREATE =================
    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<TopicResponse>> create(@Valid @RequestBody TopicRequest request) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Topic created successfully",
                        TopicMapper.toResponse(topicService.createTopic(request))
                )
        );
    }

    // ================= GET ALL =================
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<TopicResponse>>> getAll() {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Topics fetched successfully",
                        topicService.listAll()
                                .stream()
                                .map(TopicMapper::toResponse)
                                .toList()
                )
        );
    }

    // ================= WEAK =================
    @GetMapping("/weak")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<TopicResponse>>> getWeakTopics() {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Weak topics retrieved",
                        topicService.getWeakTopics()
                                .stream()
                                .map(TopicMapper::toResponse)
                                .toList()
                )
        );
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteTopic(@PathVariable String id) {
        topicService.deleteTopic(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Topic deleted successfully", null)
        );
    }

    // ================= STATS =================
    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<LearningStats>> stats() {
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Statistics retrieved", topicService.getStats())
        );
    }

    // ================= SEARCH =================
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<TopicResponse>>> search(@RequestParam String q) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Search results",
                        topicService.search(q)
                                .stream()
                                .map(TopicMapper::toResponse)
                                .toList()
                )
        );
    }

    // ================= SORTED =================
    @GetMapping("/sorted")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<TopicResponse>>> sorted() {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Sorted topics by confidence",
                        topicService.getSortedByConfidence()
                                .stream()
                                .map(TopicMapper::toResponse)
                                .toList()
                )
        );
    }

    // ================= PAGINATION =================
    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<Page<TopicResponse>>> paged(
            @RequestParam int page,
            @RequestParam int size
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Paged topics",
                        topicService.getPagedTopics(page, size)
                                .map(TopicMapper::toResponse)
                )
        );
    }

    // ================= BULK UPLOAD =================
    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BulkUploadResponse>> bulkUpload(
            @RequestBody List<TopicRequest> requests
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Bulk upload completed",
                        topicService.bulkUpload(requests)
                )
        );
    }

    // ================= REVISE =================
    @PutMapping("/{id}/revise")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<TopicResponse>> revise(
            @PathVariable String id,
            @RequestBody RevisionRequest request
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Topic revised successfully",
                        TopicMapper.toResponse(
                                topicService.updateConfidence(id, request.confidence(), request.note())
                        )
                )
        );
    }

    // ================= COMPLETE =================
    @PutMapping("/{id}/complete")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<TopicResponse>> markCompleted(@PathVariable String id) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Topic marked as completed",
                        TopicMapper.toResponse(topicService.markCompleted(id))
                )
        );
    }

    // ================= OVERDUE =================
    @GetMapping("/overdue")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<List<TopicResponse>>> overdue() {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Overdue topics retrieved",
                        topicService.getOverdueTopics()
                                .stream()
                                .map(TopicMapper::toResponse)
                                .toList()
                )
        );
    }

    // ================= DEADLINE =================
    @PutMapping("/{id}/deadline")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<TopicResponse>> updateDeadline(
            @PathVariable String id,
            @RequestBody DeadlineRequest request
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Deadline updated successfully",
                        TopicMapper.toResponse(
                                topicService.updateDeadline(id, request.deadline())
                        )
                )
        );
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<TopicResponse>> getById(@PathVariable String id) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Topic fetched successfully",
                        TopicMapper.toResponse(topicService.getById(id))
                )
        );
    }


}
