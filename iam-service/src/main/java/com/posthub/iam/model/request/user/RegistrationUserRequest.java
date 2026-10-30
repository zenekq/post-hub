package com.posthub.iam.model.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrationUserRequest(
        @NotBlank
        @Size(min = 5, max = 30)
        String username,

        @NotBlank
        @Email
        @Size(min = 6, max = 50)
        String email,

        @NotBlank
        @Size(min = 8, max = 64)
        String password,

        @NotBlank
        @Size(min = 8, max = 64)
        String confirmPassword
) {}
