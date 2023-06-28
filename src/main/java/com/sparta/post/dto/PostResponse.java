package com.sparta.post.dto;

import com.sparta.post.entity.Post;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
@Getter
public class PostResponse {
    private String title;
    private String user;
    private String content;
    private LocalDateTime createdAt;

    public PostResponse(Post post){
        this.title = post.getTitle();
        this.user = post.getUser();
        this.content = post.getContent();
        this.createdAt = LocalDateTime.now();
    }

    public PostResponse(String title, String user, String contents, Timestamp createdAt) {
        this.title = title;
        this.user = user;
        this.content = contents;
        this.createdAt = createdAt.toLocalDateTime();
    }
}
