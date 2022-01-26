package com.app.mysns.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ClientDto {
    private String username;
    private String password;
    private String name;
    private String phone;
    private Timestamp updated_at;
    private Timestamp created_at;


}

