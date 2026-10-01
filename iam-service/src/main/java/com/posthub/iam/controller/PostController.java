package com.posthub.iam.controller;

import com.posthub.iam.model.constants.ApiLogMessage;
import com.posthub.iam.model.dto.post.PostDTO;
import com.posthub.iam.model.request.post.PostRequest;
import com.posthub.iam.model.responce.ApiResult;
import com.posthub.iam.service.PostService;
import com.posthub.iam.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${end.point.posts}")
public class PostController {

    private final PostService postService;

    @GetMapping("${end.point.id}")
    public ResponseEntity<ApiResult<PostDTO>> getPostById(
            @PathVariable("id") Integer postId) {

        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        ApiResult<PostDTO> response = postService.getById(postId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("${end.point.create}")
    public ResponseEntity<ApiResult<PostDTO>> createPost(
            @RequestBody PostRequest postRequest) {

        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        ApiResult<PostDTO> response = postService.createPost(postRequest);

        return ResponseEntity.ok(response);
    }

}
