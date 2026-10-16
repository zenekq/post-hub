package com.posthub.iam.model.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

    private Integer id;
    private String title;
    private String content;
    private Integer likes;
    private LocalDateTime created;
    private String createdBy;

}
