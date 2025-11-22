package com.tracker.model;

import java.time.LocalDateTime;

public class Revision {
    private LocalDateTime revisedAt;
    private int oldConfidence;
    private int newConfidence;
    private String note;

    public Revision(LocalDateTime revisedAt, int oldConfidence, int newConfidence, String note) {
        this.revisedAt = revisedAt;
        this.oldConfidence = oldConfidence;
        this.newConfidence = newConfidence;
        this.note = note;
    }

    public LocalDateTime getRevisedAt() {
        return revisedAt;
    }

    public int getOldConfidence() {
        return oldConfidence;
    }

    public int getNewConfidence() {
        return newConfidence;
    }

    public String getNote() {
        return note;
    }
}
