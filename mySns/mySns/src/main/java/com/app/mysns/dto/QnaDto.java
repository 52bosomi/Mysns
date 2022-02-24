package com.app.mysns.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QnaDto {

    private long idx;
    private String username;
    private String title;
    private String content;
    private String password;
    private boolean disclosure;
    private String created_at;

    private String userId;

}
