package com.posthub.iam.model.request.post;

import com.posthub.iam.model.enums.CommentRestriction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewPostRequest(
        @NotBlank
        String title,

        @NotBlank
        String content,

        @NotNull
        Integer likes,

        // Optional, CommentRestriction.ALL is used when it is not set.
        CommentRestriction commentRestriction
) {}
