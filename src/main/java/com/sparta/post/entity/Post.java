package com.sparta.post.entity;

import com.sparta.post.dto.PostRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
@Getter
@ToString
@Setter
@NoArgsConstructor
public class Post {
    private Long id;
    private String title;
    private String user;
    private String password;
    private String content;
    private LocalDateTime createdAt;

    public Post(PostRequest postRequest){
        this.title = postRequest.getTitle();
        this.user = postRequest.getUser();
        this.password = postRequest.getPassword();
        this.content = postRequest.getContent();
        this.createdAt = LocalDateTime.now();
    }
}
