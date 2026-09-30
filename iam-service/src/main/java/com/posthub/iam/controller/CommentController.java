package com.posthub.iam.controller;

import com.posthub.iam.service.CommentService;
import com.posthub.iam.service.impl.CommentServiceImpl;
import com.posthub.iam.service.impl.SecondCommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    private CommentService commentService;

    @Autowired
    public void setCommentService(CommentServiceImpl commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> addComment(@RequestBody Map<String, Object> requestBody) {

        String content = (String) requestBody.get("content");
        commentService.createComment(content);

        return new ResponseEntity<>("comment added: " + content, HttpStatus.OK);
    }

    @PostMapping("/switchService")
    public ResponseEntity<String> switchToSecondComment(@RequestBody Map<String, Object> requestBody) {

        commentService = new SecondCommentServiceImpl();

        String content = (String) requestBody.get("content");
        commentService.createComment(content);

        return new ResponseEntity<>("switchService comment added: " + content, HttpStatus.OK);
    }
}
