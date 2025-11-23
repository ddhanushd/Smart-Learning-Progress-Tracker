package com.tracker.service;

import com.tracker.dto.BulkUploadResponse;
import com.tracker.dto.LearningStats;
import com.tracker.dto.TopicRequest;
import com.tracker.model.Topic;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface TopicService {
    public Topic createTopic(TopicRequest request);
    public List<Topic> listAll();
    public List<Topic> getWeakTopics();
    public Topic updateConfidence(String id, int newConfidence, String note);
    public void deleteTopic(String id);
    public LearningStats getStats();
    public List<Topic> search(String keyword);
    public List<Topic> getSortedByConfidence();
    public BulkUploadResponse bulkUpload(List<TopicRequest> requests);
    public List<Topic> getOverdueTopics();
    public Topic updateDeadline(String id, LocalDate deadline);
    public Page<Topic> getPagedTopics(int page, int size);
    public Topic markCompleted(String id);

}
