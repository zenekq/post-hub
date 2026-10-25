package com.posthub.iam.factory;

import com.posthub.iam.model.dto.role.RoleDTO;
import com.posthub.iam.model.dto.user.UserDTO;
import com.posthub.iam.model.entity.Role;
import com.posthub.iam.model.entity.User;
import com.posthub.iam.model.request.user.NewUserRequest;
import com.posthub.iam.model.request.user.UpdateUserRequest;
import com.posthub.iam.service.model.IamServiceUserRole;

import static com.posthub.iam.factory.TestDataConstants.*;

import java.util.Collection;
import java.util.List;

public final class UserTestData { ;

    public static User createTestUser() {

        User user = new User();

        user.setId(TEST_USER_ID);
        user.setUsername(TEST_USERNAME);
        user.setEmail(TEST_EMAIL);
        user.setPassword(TEST_ENCODED_PASSWORD);

        Role role = new Role();
        role.setName(IamServiceUserRole.USER.getRole());
        user.setRoles(List.of(role));

        return user;
    }

    public static UserDTO createTestUserDTO() {
        return createTestUserDTO(createTestUser());
    }

    public static UserDTO createTestUserDTO(User user) {

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreated(),
                user.getLastLogin(),
                user.getRegistrationStatus(),
                mapRoles(user.getRoles()));
    }

    public static NewUserRequest createTestNewUserRequest() {
        return new NewUserRequest(
                TEST_USERNAME_NEW_USER,
                TEST_PASSWORD,
                TEST_NEW_USER_EMAIL);
    }

    public static UpdateUserRequest createTestUpdateUserRequest() {
        return new UpdateUserRequest(
                TEST_UPDATE_USERNAME,
                TEST_UPDATE_EMAIL,
                false);
    }

    public static List<RoleDTO> mapRoles(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new RoleDTO(role.getId(), role.getName()))
                .toList();
    }
}
