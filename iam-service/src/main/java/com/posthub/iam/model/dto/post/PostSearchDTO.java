package com.posthub.iam.model.dto.post;

import java.time.LocalDateTime;

public record PostSearchDTO(
        Long id,
        String title,
        String content,
        Integer likes,
        LocalDateTime created,
        Boolean deleted,
        String createdBy
) {}
