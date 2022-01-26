package com.app.mysns.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// Data 어노테이션은 getter, setter를 자동 생성합니다.
@Data

// AllArgsConstructor 어노테이션은 생성자를 자동 생성합니다.
@AllArgsConstructor
public class SocketVO {

    public SocketVO(String username, String content, String taskKey) {
        this.taskKey = taskKey;
        this.username = username;
        this.content = content;
    }
    // 유저의 이름을 저장하기 위한 변수
    private String username;
    // 메시지의 내용을 저장하기 위한 변수
    private String content;
    // 사용자가 요청하는 작업 아이디
    private String taskKey;

    public String getUsername() { return this.username; }
    public void getUsername(String username) { this.username = username; }

    public String getContent() { return this.content; }
    public void getContent(String content) { this.content = content; }

    public String getTaskKey() { return this.taskKey; }
    public void getTaskKey(String taskKey) { this.taskKey = taskKey; }
}