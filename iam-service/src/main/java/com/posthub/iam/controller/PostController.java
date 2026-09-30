package com.posthub.iam.controller;

import com.posthub.iam.model.constants.ApiErrorMassage;
import com.posthub.iam.model.constants.ApiLogMessage;
import com.posthub.iam.model.entity.Post;
import com.posthub.iam.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${end.point.posts}")
public class PostController {

    private final PostRepository postRepository;

    @GetMapping("${end.point.id}")
    public ResponseEntity<Post> getPostById(
            @PathVariable("id") Integer postId) {

        log.info(ApiLogMessage.POST_INFO_BY_ID.getMessage(postId));

        return postRepository.findById(postId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.info(ApiErrorMassage.POST_NOT_FOUND_BY_ID.getMessage(postId));
                    return ResponseEntity.notFound().build();
                });
    }

}
