package com.posthub.iam.model.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewUserRequest(
        @NotBlank(message = "username cannot be empty")
        @Size(max = 30)
        String username,

        @NotBlank
        @Size(max = 64)
        String password,

        @NotBlank
        @Size(max = 50)
        @Email
        String email
) {}
