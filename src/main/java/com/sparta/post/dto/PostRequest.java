package com.sparta.post.dto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class PostRequest {

    private String title;
    private String user;
    private String password;
    private String content;

}
