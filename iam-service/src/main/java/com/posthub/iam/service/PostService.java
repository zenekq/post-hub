package com.posthub.iam.service;

import com.posthub.iam.model.dto.post.PostDTO;
import com.posthub.iam.model.dto.post.PostSearchDTO;
import com.posthub.iam.model.request.post.NewPostRequest;
import com.posthub.iam.model.request.post.PostSearchRequest;
import com.posthub.iam.model.request.post.UpdatePostRequest;
import com.posthub.iam.model.responce.ApiResult;
import com.posthub.iam.model.responce.PaginationResponse;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import jakarta.validation.constraints.NotNull;

public interface PostService {

    ApiResult<PostDTO> getById(@NotNull Integer id);

    ApiResult<PostDTO> createPost(@NotNull Integer userId, NewPostRequest newPostRequest);

    ApiResult<PostDTO> updatePost(@NotNull Integer postId, @NotNull UpdatePostRequest updatePostRequest);

    void softDeletePost(@NotNull Integer postId);

    ApiResult<PaginationResponse<PostSearchDTO>> findAllPosts(Pageable pageable);

    ApiResult<PaginationResponse<PostSearchDTO>> searchPosts(@NonNull PostSearchRequest postSearchRequest, Pageable pageable);

}
