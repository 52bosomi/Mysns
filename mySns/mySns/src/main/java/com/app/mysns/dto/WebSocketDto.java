package com.app.mysns.dto;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;

public class WebSocketDto {
    private String username = "";
    private String password = "";
    private String cmd = "";
    private String description = "";
    private String name = "";
    private String agentUUID = "";
    private String clientUUID = "";
    private String from = "";
    private String type = "";
    private String ua = "";
    private ArrayList<HashMap<String, String>> result;

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    
    public String getClientUUID() {
        return clientUUID;
    }
    public void setClientUUID(String clientUUID) {
        this.clientUUID = clientUUID;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
    public String getCmd() {
        return cmd;
    }
    public void setAgentUUID(String agentUUID) {
        this.agentUUID = agentUUID;
    }
    public String getAgentUUID() {
        return agentUUID;
    }

    public void setFrom(String from) {
        this.from = from;
    }
    public String getFrom() {
        return from;
    }

    // user-agent
    public void setUA(String ua) {
        this.ua = ua;
    }
    public String getUA() {
        return ua;
    }

    public void setResult(ArrayList<HashMap<String, String>> result) {
        this.result = result;
    }
    public ArrayList<HashMap<String, String>> getResult() {
        return result;
    }

    public static <T> T ToData(String value, Class<T> clazz) {
        return new Gson().fromJson(value, clazz);
        // return clazz.cast(value);
    }
    
    public String ToJson() { return new Gson().toJson(this) ; }
}
