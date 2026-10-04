package com.posthub.iam.model.request.user;

import com.posthub.iam.model.enums.UserSortField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchRequest {

    private String username;
    private String email;
    private Boolean deleted = false;
    private String keyword;
    private UserSortField sortField;

}
