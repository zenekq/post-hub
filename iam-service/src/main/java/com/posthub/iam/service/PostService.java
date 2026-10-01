package com.posthub.iam.service;

import com.posthub.iam.model.dto.post.PostDTO;
import com.posthub.iam.model.request.post.NewPostRequest;
import com.posthub.iam.model.request.post.UpdatePostRequest;
import com.posthub.iam.model.responce.ApiResult;
import jakarta.validation.constraints.NotNull;

public interface PostService {

    ApiResult<PostDTO> getById(@NotNull Integer id);

    ApiResult<PostDTO> createPost(@NotNull NewPostRequest newPostRequest);

    ApiResult<PostDTO> updatePost(@NotNull Integer postId, @NotNull UpdatePostRequest updatePostRequest);

    void softDeletePost(@NotNull Integer postId);

}
