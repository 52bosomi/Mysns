package com.app.mysns.dto;

import java.sql.Timestamp;

public class UserDto {

    private String username;
    private String password;
    private String name;
    private boolean use_facebook;
    private String access_facebook;
    private String refresh_facebook;
    private boolean use_instagram;
    private String access_instagram;
    private boolean use_naver;
    private String access_naver;
    private String refresh_naver;
    private Timestamp updated_at;
    private Timestamp created_at;

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

    public boolean isUse_facebook() {
        return use_facebook;
    }

    public void setUse_facebook(boolean use_facebook) {
        this.use_facebook = use_facebook;
    }

    public String getAccess_facebook() {
        return access_facebook;
    }

    public void setAccess_facebook(String access_facebook) {
        this.access_facebook = access_facebook;
    }

    public String getRefresh_facebook() {
        return refresh_facebook;
    }

    public void setRefresh_facebook(String refresh_facebook) {
        this.refresh_facebook = refresh_facebook;
    }

    public boolean isUse_instagram() {
        return use_instagram;
    }

    public void setUse_instagram(boolean use_instagram) {
        this.use_instagram = use_instagram;
    }

    public String getAccess_instagram() {
        return access_instagram;
    }

    public void setAccess_instagram(String access_instagram) {
        this.access_instagram = access_instagram;
    }

    public boolean isUse_naver() {
        return use_naver;
    }

    public void setUse_naver(boolean use_naver) {
        this.use_naver = use_naver;
    }

    public String getAccess_naver() {
        return access_naver;
    }

    public void setAccess_naver(String access_naver) {
        this.access_naver = access_naver;
    }

    public String getRefresh_naver() {
        return refresh_naver;
    }

    public void setRefresh_naver(String refresh_naver) {
        this.refresh_naver = refresh_naver;
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
