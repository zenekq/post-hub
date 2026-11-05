package com.posthub.iam.model.request.post;

import com.posthub.iam.model.enums.CommentRestriction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdatePostRequest(
        @NotBlank
        String title,

        @NotBlank
        String content,

        @NotNull
        Integer likes,

        // Optional, current restriction is kept when it is not set.
        CommentRestriction commentRestriction
) {}
