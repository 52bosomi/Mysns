package com.app.mysns.dto;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClientDto {
    private long idx;
    private String username;
    private String password;
    private String name;
    private String phone;
    private Timestamp updated_at;
    private Timestamp created_at;

    // public ClientDto(String username, String password, String name, String phone) {
    //     this.username = username;
    //     this.password = password;
    //     this.name = name;
    //     this.phone = phone;
    // }
    
    public long getIdx() {
        return this.idx;
    }
    public void setIdx(long idx) {
        this.idx = idx;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
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

