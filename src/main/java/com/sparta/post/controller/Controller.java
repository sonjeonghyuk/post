//package com.sparta.post.controlloer;
//
//import com.sparta.post.dto.PostRequest;
//import com.sparta.post.dto.PostResponse;
//import com.sparta.post.entity.Post;
//import lombok.RequiredArgsConstructor;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.jdbc.support.GeneratedKeyHolder;
//import org.springframework.jdbc.support.KeyHolder;
//import org.springframework.web.bind.annotation.*;
//
//import java.sql.*;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api")
//@RequiredArgsConstructor    // jdbcTemplate
//public class PostController {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    @GetMapping("/post")
//    public List<PostResponse> getAllPosts() {
//
//        String sql = "SELECT * FROM post ORDER BY createdAt DESC ";
//
//        return jdbcTemplate.query(sql, new RowMapper<PostResponse>() {
//            @Override
//            public PostResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
//                // SQL 의 결과로 받아온 Post 데이터들을 PostResponseDto 타입으로 변환해줄 메서드
//                String title = rs.getString("title");
//                String writer = rs.getString("writer");
//                String content = rs.getString("content");
//                Timestamp createdAt = rs.getTimestamp("createdAt");
//                return new PostResponse(title, writer, content, createdAt);
//            }
//        });
//
//    }
//
//    @PostMapping("/post")   // 작성
//    public PostResponse createPosts(@RequestBody PostRequest postRequest) {
//        // 받은 PostRequest -> Entity 로
//        Post newPost = new Post(postRequest); // Entity
//
//        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체: 키를 만들어주는 애
//
//        String sql = "INSERT INTO post (title, writer, password, content, createdAt) VALUES (?, ?, ?, ?, ?)";
//        jdbcTemplate.update(con -> {
//                    PreparedStatement preparedStatement = con.prepareStatement(sql,
//                            Statement.RETURN_GENERATED_KEYS);
//
//                    preparedStatement.setString(1, newPost.getTitle());
//                    preparedStatement.setString(2, newPost.getWriter());
//                    preparedStatement.setString(3, newPost.getPassword());
//                    preparedStatement.setString(4, newPost.getContent());
//                    preparedStatement.setTimestamp(5, Timestamp.valueOf(newPost.getCreatedAt()));
//
//                    return preparedStatement;
//                },
//                keyHolder);
//
//        PostResponse postResponse = new PostResponse(newPost);
//
//        return postResponse;
//    }
//
//
//    @GetMapping("/post/{id}")
//    public PostResponse getPosts(@PathVariable Long id) {
//        // findbyId
//        Post post = findById(id);
//        if (post != null) {
//            PostResponse postResponse = new PostResponse(post);
//            return postResponse;
//
//        } else {
//            throw new IllegalArgumentException("선택한 게시물은 존재하지 않습니다.");
//        }
//    }
//
//    @PutMapping("/post/{id}")
//    public Long updatePost(@PathVariable Long id, @RequestBody PostRequest requestDto) {
//        // 해당 게시물이 DB에 존재하는지 확인
//        Post post = findById(id);
////        System.out.println("post.getPassword() = " + post.getPassword());
////        System.out.println("requestDto = " + requestDto.getPassword());
//        if (post != null) {
//            if (requestDto.getPassword().equals(post.getPassword())) {
//                // post 내용 수정
//                String sql = "UPDATE post SET title = ?, writer = ?, content = ? WHERE id = ?";
//                jdbcTemplate.update(sql, requestDto.getTitle(), requestDto.getWriter(), requestDto.getContent(), id);
//
//                return id;
//            } else {
//                throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
//            }
//        } else {
//            throw new IllegalArgumentException("선택한 게시물은 존재하지 않습니다.");
//        }
//    }
//
//    @DeleteMapping("/post/{id}")
//    public Long deletePost(@PathVariable Long id, @RequestBody PostRequest requestDto) {
//        // 해당 게시물이 DB에 존재하는지 확인
//        Post post = findById(id);
//        if (post != null) {
//            if (requestDto.getPassword().equals(post.getPassword())) {
//                // post 삭제
//                String sql = "DELETE FROM post WHERE id = ?";
//                jdbcTemplate.update(sql, id);
//
//                return id;
//            } else {
//                throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
//            }
//        } else {
//            throw new IllegalArgumentException("선택한 게시물은 존재하지 않습니다.");
//        }
//    }
//
//    private Post findById(Long id) {
//        // DB 조회
//        String sql = "SELECT * FROM post WHERE id = ?";
//
//        return jdbcTemplate.query(sql, resultSet -> {
//            if (resultSet.next()) {
//                Post post = new Post();
//                post.setTitle(resultSet.getString("title"));
//                post.setWriter(resultSet.getString("writer"));
//                post.setContent(resultSet.getString("content"));
//                post.setPassword(resultSet.getString("password"));
//                post.setCreatedAt(resultSet.getTimestamp("createdAt").toLocalDateTime());
//                return post;
//            } else {
//                return null;
//            }
//        }, id);
//    }
//
//}