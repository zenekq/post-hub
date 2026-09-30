package com.posthub.iam.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final List<String> post = new ArrayList<>();

    @Override
    public void createPost(String postContent) {
        post.add(postContent);
    }
}
