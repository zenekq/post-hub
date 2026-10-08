package com.posthub.iam.model.dto.comment;

import com.posthub.iam.model.dto.post.PostOwnerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    private Integer id;
    private String message;
    private PostOwnerDTO postOwnerDTO;
    private Integer postId;
    private LocalDateTime created;
    private LocalDateTime updated;

}
