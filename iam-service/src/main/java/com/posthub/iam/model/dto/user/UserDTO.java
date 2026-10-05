package com.posthub.iam.model.dto.user;

import com.posthub.iam.model.dto.role.RoleDTO;
import com.posthub.iam.model.enums.RegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {

    private Integer id;
    private String username;
    private String email;
    private LocalDateTime created;
    private LocalDateTime lastLogin;
    private RegistrationStatus registrationStatus;
    private List<RoleDTO> roles;

}
