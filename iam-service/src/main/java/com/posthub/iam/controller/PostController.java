package com.posthub.iam.controller;

import com.posthub.iam.model.constants.ApiLogMessage;
import com.posthub.iam.model.dto.post.PostDTO;
import com.posthub.iam.model.dto.post.PostSearchDTO;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@Validated
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
            @RequestBody @Valid NewPostRequest newPostRequest, Principal principal) {

        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        ApiResult<PostDTO> response = postService.createPost(newPostRequest, principal.getName());

        return ResponseEntity.ok(response);
    }

    @PutMapping("${end.point.id}")
    public ResponseEntity<ApiResult<PostDTO>> updatePostById(
            @PathVariable("id") Integer postId,
            @RequestBody @Valid UpdatePostRequest updatePostRequest) {

        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        ApiResult<PostDTO> response = postService.updatePost(postId, updatePostRequest);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("${end.point.id}")
    public ResponseEntity<Void> deletePostById(
            @PathVariable("id") Integer postId) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        postService.softDeletePost(postId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("${end.point.all}")
    public ResponseEntity<ApiResult<PaginationResponse<PostSearchDTO>>> getAllPosts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        Pageable pageable = PageRequest.of(page, limit);
        ApiResult<PaginationResponse<PostSearchDTO>> allPosts = postService.findAllPosts(pageable);
        return ResponseEntity.ok(allPosts);
    }

    @PostMapping("${end.point.search}")
    public ResponseEntity<ApiResult<PaginationResponse<PostSearchDTO>>> searchPosts(
            @RequestBody @Valid PostSearchRequest postSearchRequest,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit) {

        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        Pageable pageable = PageRequest.of(page, limit);
        ApiResult<PaginationResponse<PostSearchDTO>> respose = postService.searchPosts(postSearchRequest, pageable);

        return ResponseEntity.ok(respose);
    }


}
