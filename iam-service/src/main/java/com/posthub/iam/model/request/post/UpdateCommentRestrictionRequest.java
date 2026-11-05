package com.posthub.iam.model.request.post;

import com.posthub.iam.model.enums.CommentRestriction;
import jakarta.validation.constraints.NotNull;

public record UpdateCommentRestrictionRequest(
        @NotNull
        CommentRestriction commentRestriction
) {}
