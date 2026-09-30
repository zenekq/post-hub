package com.posthub.iam.controller;

import com.posthub.iam.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService dafaultCommentService;
    private final CommentService advancedCommentService;

    @Autowired
    public CommentController(
            CommentService commentService,
            @Qualifier("advancedCommentService") CommentService advancedCommentService) {
        this.dafaultCommentService = commentService;
        this.advancedCommentService = advancedCommentService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> addComment(@RequestBody Map<String, Object> requestBody) {

        String content = (String) requestBody.get("content");
        dafaultCommentService.createComment(content);

        return new ResponseEntity<>("comment added: " + content, HttpStatus.OK);
    }

    @PostMapping("/createAdvanced")
    public ResponseEntity<String> addCommentAdvanced(@RequestBody Map<String, Object> requestBody) {

        String content = (String) requestBody.get("content");
        advancedCommentService.createComment(content);

        return new ResponseEntity<>("createAdvanced comment added: " + content, HttpStatus.OK);
    }
}
