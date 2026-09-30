package com.posthub.iam.service.impl;

import com.posthub.iam.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SecondCommentServiceImpl implements CommentService {

    @Override
    public void createComment(String commentContent) {

        System.out.println("Advanced comment create: " + commentContent);
    }
}
