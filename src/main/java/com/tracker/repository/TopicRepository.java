package com.tracker.repository;

import com.tracker.model.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface TopicRepository extends MongoRepository<Topic, String> {
    List<Topic> findByStatus(String status);
    List<Topic> findByNameContainingIgnoreCase(String name);
    List<Topic> findAllByOrderByConfidenceDesc();
    List<Topic> findByDeadlineBeforeAndCompletedFalse(LocalDate date);


}
