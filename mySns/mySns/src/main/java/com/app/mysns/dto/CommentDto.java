package com.app.mysns.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CommentDto {

    private int seq;
    private long idx;
    private String comment;
    private String writer;
    private Timestamp date;

}
