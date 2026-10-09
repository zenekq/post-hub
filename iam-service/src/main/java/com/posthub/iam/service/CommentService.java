package com.posthub.iam.service;

import com.posthub.iam.model.dto.comment.CommentDTO;
import com.posthub.iam.model.dto.comment.CommentSearchDTO;
import com.posthub.iam.model.request.comment.CommentRequest;
import com.posthub.iam.model.request.comment.CommentSearchRequest;
import com.posthub.iam.model.request.comment.UpdateCommentRequest;
import com.posthub.iam.model.responce.ApiResult;
import com.posthub.iam.model.responce.PaginationResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    ApiResult<CommentDTO> getCommentById(@NotNull Integer commentId);

    ApiResult<CommentDTO> createComment(@NotNull CommentRequest commentRequest);

    ApiResult<CommentDTO> updateComment(@NotNull Integer commentId, @NotNull UpdateCommentRequest request);

    void softDelete(@NotNull Integer commentId);

    ApiResult<PaginationResponse<CommentSearchDTO>> findAllComments(Pageable pageable);

    ApiResult<PaginationResponse<CommentSearchDTO>> searchComments(@NotNull CommentSearchRequest request, Pageable pageable);

}
