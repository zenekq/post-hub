package com.posthub.iam.controller;

import com.posthub.iam.model.constants.ApiLogMessage;
import com.posthub.iam.model.dto.post.PostDTO;
import com.posthub.iam.model.dto.post.PostSearchDTO;
import com.posthub.iam.model.request.pagination.PaginationRequest;
import com.posthub.iam.model.request.post.NewPostRequest;
import com.posthub.iam.model.request.post.PostSearchRequest;
import com.posthub.iam.model.request.post.UpdatePostRequest;
import com.posthub.iam.model.responce.ApiResult;
import com.posthub.iam.model.responce.PaginationResponse;
import com.posthub.iam.service.PostService;
import com.posthub.iam.utils.ApiUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("${end.points.posts}")
public class PostController {

    private final PostService postService;

    @GetMapping("${end.points.id}")
    public ResponseEntity<ApiResult<PostDTO>> getPostById(
            @PathVariable("id") Integer postId) {

        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), ApiUtils.getMethodName());

        ApiResult<PostDTO> response = postService.getById(postId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("${end.points.create}")
    public ResponseEntity<ApiResult<PostDTO>> createPost(
            @RequestBody @Valid NewPostRequest newPostRequest) {

        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), ApiUtils.getMethodName());

        ApiResult<PostDTO> response = postService.createPost(newPostRequest);

        return ResponseEntity.ok(response);
    }

    @PutMapping("${end.points.id}")
    public ResponseEntity<ApiResult<PostDTO>> updatePostById(
            @PathVariable("id") Integer postId,
            @RequestBody @Valid UpdatePostRequest updatePostRequest) {

        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), ApiUtils.getMethodName());

        ApiResult<PostDTO> response = postService.updatePost(postId, updatePostRequest);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("${end.points.id}")
    public ResponseEntity<Void> deletePostById(
            @PathVariable("id") Integer postId) {

        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), ApiUtils.getMethodName());

        postService.softDeletePost(postId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("${end.points.all}")
    public ResponseEntity<ApiResult<PaginationResponse<PostSearchDTO>>> getAllPosts(
            @ModelAttribute PaginationRequest paginationRequest) {

        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), ApiUtils.getMethodName());

        ApiResult<PaginationResponse<PostSearchDTO>> allPosts = postService.findAllPosts(paginationRequest);
        return ResponseEntity.ok(allPosts);
    }

    @PostMapping("${end.points.search}")
    public ResponseEntity<ApiResult<PaginationResponse<PostSearchDTO>>> searchPosts(
            @RequestBody @Valid PostSearchRequest postSearchRequest,
            @ModelAttribute PaginationRequest paginationRequest) {

        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), ApiUtils.getMethodName());

        ApiResult<PaginationResponse<PostSearchDTO>> response = postService.
                searchPosts(postSearchRequest, paginationRequest);

        return ResponseEntity.ok(response);
    }

}
