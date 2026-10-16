package com.posthub.iam.model.dto.post;

import java.time.LocalDateTime;

public record PostDTO(
        Integer id,
        String title,
        String content,
        Integer likes,
        LocalDateTime created,
        String createdBy
) {}
