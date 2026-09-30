package com.posthub.iam.service.impl;

import com.posthub.iam.service.CommentService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class CommentServiceImpl implements CommentService {

    private final List<String> comments =  new ArrayList<>();

    @Override
    public void createComment(String commentContent) {
        comments.add(commentContent);

        System.out.println("CommentServiceImpl Post saved: " + commentContent);
    }
}
