package com.posthub.iam.model.request.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateCommentRequest(
        @NotNull(message = "Post ID cannot be null")
        Long postId,

        @NotBlank(message = "Content cannot be empty")
        String message
) {}
