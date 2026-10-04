package com.posthub.iam.model.dto.user;

import com.posthub.iam.model.enums.RegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchDTO implements Serializable {

    private Integer id;
    private String username;
    private String email;
    private Boolean deleted;

}
