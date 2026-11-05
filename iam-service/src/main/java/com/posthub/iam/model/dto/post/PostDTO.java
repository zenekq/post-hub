package com.posthub.iam.model.dto.post;

import com.posthub.iam.model.enums.CommentRestriction;

import java.time.LocalDateTime;

public record PostDTO(
        Long id,
        String title,
        String content,
        Integer likes,
        LocalDateTime created,
        String createdBy,
        CommentRestriction commentRestriction
) {}
