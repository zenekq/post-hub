package com.posthub.iam.model.dto.user;

import com.posthub.iam.model.dto.role.RoleDTO;
import com.posthub.iam.model.enums.RegistrationStatus;

import java.time.LocalDateTime;
import java.util.List;

public record UserSearchDTO(
        Long id,
        String username,
        String email,
        LocalDateTime created,
        Boolean deleted,
        RegistrationStatus registrationStatus,
        List<RoleDTO> roles
) {}

