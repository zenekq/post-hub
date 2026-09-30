package com.posthub.iam.controller;

import com.posthub.iam.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postServiceImpl) {
        this.postService = postServiceImpl;
    }


    @PostMapping("/create")
    public ResponseEntity<String> createPost(@RequestBody Map<String, Object> requestBody) {
        String title = (String) requestBody.get("title");
        String content = (String) requestBody.get("content");

        String postContent = "Title: " + title + "\nContent: " + content + "\n";

        postService.createPost(postContent);

        return new ResponseEntity<>("Post created with title: " + title, HttpStatus.OK);
    }

}
