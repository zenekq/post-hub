package com.posthub.iam.model.request.post;

import com.posthub.iam.model.enums.PostSortField;

public record PostSearchRequest(
        String title,
        String content,
        Integer likes,
        Boolean deleted,
        String keyword,
        PostSortField sortField
) {}
