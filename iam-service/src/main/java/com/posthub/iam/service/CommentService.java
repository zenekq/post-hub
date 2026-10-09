package com.posthub.iam.service;

import com.posthub.iam.model.dto.comment.CommentDTO;
import com.posthub.iam.model.request.comment.CommentRequest;
import com.posthub.iam.model.responce.ApiResult;
import jakarta.validation.constraints.NotNull;

public interface CommentService {

    ApiResult<CommentDTO> getCommentById(@NotNull Integer commentId);

    ApiResult<CommentDTO> createComment(@NotNull CommentRequest commentRequest);

}
