package com.posthub.iam.model.dto.user;

import com.posthub.iam.model.dto.role.RoleDTO;
import com.posthub.iam.model.enums.RegistrationStatus;

import java.time.LocalDateTime;
import java.util.List;

public record UserProfileDTO(
        Long id,
        String username,
        String email,
        RegistrationStatus registrationStatus,
        LocalDateTime lastLogin,
        String token,
        String refreshToken,
        List<RoleDTO> roles
) {}
