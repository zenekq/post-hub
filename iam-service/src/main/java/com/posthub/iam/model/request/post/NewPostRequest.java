package com.posthub.iam.model.request.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewPostRequest(
        @NotBlank
        String title,

        @NotBlank
        String content,

        @NotNull
        Integer likes
) {}
