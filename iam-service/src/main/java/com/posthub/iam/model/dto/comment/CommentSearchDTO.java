package com.posthub.iam.model.dto.comment;

import com.posthub.iam.model.dto.post.PostOwnerDTO;

import java.time.LocalDateTime;

public record CommentSearchDTO(
        Long id,
        String message,
        PostOwnerDTO owner,
        Integer postId,
        LocalDateTime created,
        LocalDateTime updated,
        boolean deleted
) {}
