package com.posthub.iam.service;

import com.posthub.iam.model.dto.post.PostDTO;
import com.posthub.iam.model.request.post.PostRequest;
import com.posthub.iam.model.responce.ApiResult;
import jakarta.validation.constraints.NotNull;

public interface PostService {

    ApiResult<PostDTO> getById(@NotNull Integer id);

    ApiResult<PostDTO> createPost(@NotNull PostRequest postRequest);

}
