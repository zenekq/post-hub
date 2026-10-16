package com.posthub.iam.model.request.user;

import com.posthub.iam.model.enums.UserSortField;

public record UserSearchRequest(
        String username,
        String email,
        Boolean deleted,
        String keyword,
        UserSortField sortField
) {
    public UserSearchRequest {
        if (deleted == null) deleted = false;
    }
}
