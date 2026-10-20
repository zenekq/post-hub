package com.posthub.iam.model.dto.comment;

import com.posthub.iam.model.dto.post.PostOwnerDTO;

import java.time.LocalDateTime;

public record CommentDTO(
        Long id,
        String message,
        PostOwnerDTO owner,
        Long postId,
        LocalDateTime created,
        LocalDateTime updated
) {}
