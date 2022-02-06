package com.app.mysns.dto;

import java.security.Timestamp;

public class AgentDto {
    private long idx;
    private String uuid;
    private String description;
    private Timestamp updated_at;
    private Timestamp created_at;
    
    public long getIdx() {
        return this.idx;
    }
    public void setIdx(long idx) {
        this.idx = idx;
    }

    public String getUUID() {
        return uuid;
    }
    public void setUUID(String uuid) {
        this.uuid = uuid;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
