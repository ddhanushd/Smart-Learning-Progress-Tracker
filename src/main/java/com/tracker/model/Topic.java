package com.tracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "topics")
public class Topic {

    @Id
    private String id;

    private String name;

    private int confidence;

    private String status;     // STRONG / AVERAGE / WEAK
    private String suggestion; // Text message

    private List<Revision> revisions = new ArrayList<>();

    private LocalDate deadline;
    private boolean completed;

    public Topic() {
    }

    public Topic(String name, int confidence, String status, String suggestion) {
        this.name = name;
        this.confidence = confidence;
        this.status = status;
        this.suggestion = suggestion;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getConfidence() {
        return confidence;
    }

    public String getStatus() {
        return status;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }
    public List<Revision> getRevisions() {
        return revisions;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
