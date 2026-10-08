package com.posthub.iam.model.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostOwnerDTO {

    private Integer id;
    private String username;
    private String email;

}
