package com.posthub.iam.model.request.comment;

import com.posthub.iam.model.enums.CommentSortField;

public record CommentSearchRequest(
        String message,
        String cratedBy,
        Integer postId,
        Boolean deleted,
        String keyword,
        CommentSortField sortField
) {}
