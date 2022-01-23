package com.app.mysns.dto;

import lombok.Data;

@Data
public class SnsTypeDto {
    private int id;
    private String name;
    private String description;

    public int getId() {
        return this.id;
    }
    public void setid(int id) {
        this.id = id;
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
