package com.posthub.iam.model.request.post;

import com.posthub.iam.model.enums.PostSortField;
import lombok.Data;

@Data
public class PostSearchRequest {

    private String title;
    private String content;
    private Integer likes;

    private Boolean deleted;
    private String keyword;
    private PostSortField sortField;

}
