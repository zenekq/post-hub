package com.posthub.iam.controller;

import com.posthub.iam.model.constants.ApiLogMessage;
import com.posthub.iam.model.dto.post.PostDTO;
import com.posthub.iam.model.responce.ApiResult;
import com.posthub.iam.service.PostService;
import com.posthub.iam.utils.ApiUtils;
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

    private final PostService postService;

    @GetMapping("${end.point.id}")
    public ResponseEntity<ApiResult<PostDTO>> getPostById(
            @PathVariable("id") Integer postId) {

        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        ApiResult<PostDTO> response = postService.getById(postId);

        return ResponseEntity.ok(response);
    }

}
