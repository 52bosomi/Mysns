package com.app.mysns.dto;

import lombok.Data;

@Data
public class SnsTypeDto {
    private int idx;
    private String name;
    private String description;

    public int getIdx() {
        return this.idx;
    }
    public void setid(int idx) {
        this.idx = idx;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
