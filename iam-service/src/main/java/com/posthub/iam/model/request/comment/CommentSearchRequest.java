package com.posthub.iam.model.request.comment;

import com.posthub.iam.model.enums.CommentSortField;
import lombok.Data;

@Data
public class CommentSearchRequest {

    private  String message;
    private String cratedBy;
    private Integer postId;

    private Boolean deleted;
    private String keyword;
    private CommentSortField sortField;

}
