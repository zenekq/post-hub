package com.posthub.iam.model.request.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostRequest {

   @NotBlank
   private String title;

   @NotBlank
   private String content;

   @NotNull
   private Integer likes;
}
