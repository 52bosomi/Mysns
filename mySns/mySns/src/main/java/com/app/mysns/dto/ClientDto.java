package com.app.mysns.dto;

import java.sql.Timestamp;

import lombok.Data;
@Data
public class ClientDto {
    private int id;
    private String username;
    private String password;
    private String name;
    //private String phone;
    private Boolean use_facebook;  
    private String access_facebook;
    private String refresh_facebook; 
    private Boolean use_instagram;  
    private String access_instagram;
    private Boolean use_naver;  
    private String access_naver;
    private String refresh_naver;
    private Timestamp updated_at;
    private Timestamp created_at;
}

