package com.posthub.iam.model.dto.comment;

import com.posthub.iam.model.dto.post.PostOwnerDTO;

import java.time.LocalDateTime;

public record CommentDTO(
        Integer id,
        String message,
        PostOwnerDTO owner,
        Integer postId,
        LocalDateTime created,
        LocalDateTime updated
) {}
