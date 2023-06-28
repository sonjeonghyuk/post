package com.sparta.post.controller;

import com.sparta.post.dto.PostRequest;
import com.sparta.post.dto.PostResponse;
import com.sparta.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {
    private final JdbcTemplate jdbcTemplate;

    PostController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/post")
    public List<PostResponse> getAllPosts() {

        String sql = "SELECT * FROM post ORDER BY createdAt DESC ";

        return jdbcTemplate.query(sql, new RowMapper<PostResponse>() {
            @Override
            public PostResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 Post 데이터들을 PostResponseDto 타입으로 변환해줄 메서드
                String title = rs.getString("title");
                String writer = rs.getString("writer");
                String content = rs.getString("content");
                Timestamp createdAt = rs.getTimestamp("createdAt");
                return new PostResponse(title, writer, content, createdAt);
            }
        });

    }

    @PostMapping("/post") // 어떻게 요청을 보낼건지 알려줄게.
    public PostResponse getPost(@RequestBody PostRequest postRequest) {// 요청이 오면 뭘 할지 작성해줘야한다 -> 메서드
        // 1. DB 데이터 형태에 맞게 무리가 받은 요청 데이터를 변환해줘야 한다.
        Post newPost = new Post(postRequest);
        System.out.println(newPost);
        // 2. DB에 저장을 한다.
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO post (title, user, password, content, createdAt) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, newPost.getTitle());
                    preparedStatement.setString(2, newPost.getUser());
                    preparedStatement.setString(3, newPost.getPassword());
                    preparedStatement.setString(4, newPost.getContent());
                    preparedStatement.setTimestamp(5, Timestamp.valueOf(newPost.getCreatedAt()));

                    return preparedStatement;
                },
                keyHolder);

        PostResponse postResponse = new PostResponse(newPost);
        return postResponse;
        // 저희가 받은 형태랑 -> DB에 맞는 형태
        // 저희가 DB에 저장할 수 있는 형태랑 DB를 기준으로
        // DB 응답 -> 저희가 응답해줄 형태랑
    }

    @GetMapping("/post/{id}")
    public PostResponse getPosts(@PathVariable Long id) {
        // findbyId
        Post post = findById(id);
        if (post != null) {
            PostResponse postResponse = new PostResponse(post);
            return postResponse;

        } else {
            throw new IllegalArgumentException("선택한 게시물은 존재하지 않습니다.");
        }
    }


    @PutMapping("/post/{id}")
    public Long updatePost(@PathVariable Long id, @RequestBody PostRequest requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        Post post = findById(id);

        if (post != null) {
            if (requestDto.getPassword().equals(post.getPassword())) {
                // memo 내용 수정
                String sql = "UPDATE post SET title = ?, user = ?, content = ? WHERE id = ?";
                jdbcTemplate.update(sql, requestDto.getTitle(), requestDto.getUser(), requestDto.getContent(), id);

                return id;
            } else {
                throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
            }
        } else {
            throw new IllegalArgumentException("선택한 게시물은 존재하지 않습니다.");
        }
    }


    @DeleteMapping("/post/{id}")
    public Long deletePost(@PathVariable Long id, @RequestBody PostRequest requestDto) {
        // 해당 게시물이 DB에 존재하는지 확인
        Post post = findById(id);
        if (post != null) {
            if (requestDto.getPassword().equals(post.getPassword())) {
                // post 삭제
                String sql = "DELETE FROM post WHERE id = ?";
                jdbcTemplate.update(sql, id);

                return id;
            } else {
                throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
            }
        } else {
            throw new IllegalArgumentException("선택한 게시물은 존재하지 않습니다.");
        }
    }

    private Post findById(Long id) {
        // DB 조회
        String sql = "SELECT * FROM post WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                Post post = new Post();
                post.setTitle(resultSet.getString("title"));
                post.setUser(resultSet.getString("User"));
                post.setContent(resultSet.getString("content"));
                post.setPassword(resultSet.getString("password"));
                post.setCreatedAt(resultSet.getTimestamp("createdAt").toLocalDateTime());
                return post;
            } else {
                return null;
            }
        }, id);
    }
}
