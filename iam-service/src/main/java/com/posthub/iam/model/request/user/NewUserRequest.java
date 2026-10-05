package com.posthub.iam.model.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {

    @NotBlank(message = "username cannot be empty")
    @Size(max = 30)
    private String username;

    @NotBlank
    @Size(max = 64)
    private String password;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
}
