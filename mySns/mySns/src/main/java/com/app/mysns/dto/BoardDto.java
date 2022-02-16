package com.app.mysns.dto;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardDto {
    private long idx;
    private long user_id;
    private boolean is_answer = false;
    private String title;
    private String question;
    private String answer;
    // private String description;
    private Timestamp updated_at;
    private Timestamp created_at;
    
    public long getIdx() {
        return this.idx;
    }
    public void setIdx(long idx) {
        this.idx = idx;
    }

    public long getUserId() {
        return user_id;
    }
    public void setUserId(long user_id) {
        this.user_id = user_id;
    }

    public Boolean getIsAnswer() {
        return is_answer;
    }
    public void setIsAgetIsAnswer(Boolean is_answer) {
        this.is_answer = is_answer;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
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

