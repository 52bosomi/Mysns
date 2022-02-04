package com.app.mysns.dto;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SyncSiteDto {
    private long idx;
    private long sns_type_id;
    private long user_id;
    private String url;
    // private String name;
    // private String description;
    private Timestamp updated_at;
    private Timestamp created_at;
    
    public long getIdx() {
        return this.idx;
    }
    public void setIdx(long idx) {
        this.idx = idx;
    }

    public long getSnnsTypeId() {
        return sns_type_id;
    }
    public void setSnnsTypeId(long sns_type_id) {
        this.sns_type_id = sns_type_id;
    }
    public long getUserId() {
        return user_id;
    }
    public void setUserId(long user_id) {
        this.user_id = user_id;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    
    public Timestamp getUpdated_at() {
        return updated_at;
    }
    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }
    public Timestamp getCreated_at() {
        return created_at;
    }
    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}

