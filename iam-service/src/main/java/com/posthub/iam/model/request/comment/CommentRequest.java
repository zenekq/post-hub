package com.posthub.iam.model.request.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {

    @NotNull(message = "Post ID cannot be null")
    private Integer postId;

    @NotBlank(message = "Content cannot be empty")
    private String message;

}
