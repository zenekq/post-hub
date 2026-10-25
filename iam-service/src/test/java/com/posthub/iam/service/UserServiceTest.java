package com.posthub.iam.service;


import com.posthub.iam.factory.UserTestData;
import com.posthub.iam.mapper.UserMapper;
import com.posthub.iam.model.dto.user.UserDTO;
import com.posthub.iam.model.entity.Role;
import com.posthub.iam.model.entity.User;
import com.posthub.iam.model.exception.DataExistException;
import com.posthub.iam.model.exception.NotFoundException;
import com.posthub.iam.model.request.user.NewUserRequest;
import com.posthub.iam.repository.RoleRepository;
import com.posthub.iam.repository.UserRepository;
import com.posthub.iam.service.impl.UserServiceImpl;
import com.posthub.iam.service.model.IamServiceUserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class UserServiceTest {

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserDTO testUserDTO;
    private Role superAdminRole;

    private static final long USER_ID = 1;

    @BeforeEach
    void setUp() {
        superAdminRole = new Role();
        superAdminRole.setName(IamServiceUserRole.SUPER_ADMIN.getRole());

        testUser = new User();
        testUser.setId(USER_ID);
        testUser.setUsername("TestUser");
        testUser.setEmail("TestUser@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setRoles(Collections.singleton(superAdminRole));

        testUserDTO = UserTestData.createTestUserDTO(testUser);
    }

    @Test
    void getById_UserExists_ReturnsUserDTO() {
        when(userRepository.findByIdAndDeletedFalse(USER_ID)).thenReturn(Optional.of(testUser));
        when(userMapper.userToUserDTO(testUser)).thenReturn(testUserDTO);

        UserDTO result = userService.getById(USER_ID).getPayload();

        assertNotNull(result);
        assertEquals(testUserDTO.id(), result.id());
        assertEquals(testUserDTO.username(), result.username());
        assertEquals(testUserDTO.email(), result.email());

        verify(userRepository, times(1)).findByIdAndDeletedFalse(USER_ID);
        verify(userRepository, times(1)).findByIdAndDeletedFalseOrThrow(USER_ID);
        verify(userMapper, times(1)).userToUserDTO(testUser);
    }

    @Test
    void getById_UserNotFound_ThrowException() {

        final long NON_EXISTENT_USER_ID = 999;

        when(userRepository.findByIdAndDeletedFalse(NON_EXISTENT_USER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(NON_EXISTENT_USER_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("not found");

        verify(userRepository, times(1)).findByIdAndDeletedFalse(NON_EXISTENT_USER_ID);
        verify(userMapper, times(0)).userToUserDTO(any(User.class));
    }

    @Test
    void createUser_AsSuperAdmin_CreatesUserSuccessfully() {
        NewUserRequest request = new NewUserRequest("NewUser", "password123!", "new_user@example.com");

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(userRepository.existsByUsername(request.username())).thenReturn(false);
        when(roleRepository.findByName(IamServiceUserRole.USER.getRole())).thenReturn(Optional.of(superAdminRole));

        User newUser = new User();
        newUser.setUsername(request.username());
        newUser.setEmail(request.email());
        newUser.setPassword("encodedPassword");
        newUser.setRoles(Collections.singleton(superAdminRole));

        when(userMapper.createUser(request)).thenReturn(newUser);

        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        when(userMapper.userToUserDTO(newUser)).thenReturn(testUserDTO);

        UserDTO result = userService.createUser(request).getPayload();

        assertNotNull(result);
        assertEquals(testUserDTO.id(), result.id());
        assertEquals(testUserDTO.username(), result.username());

        verify(userRepository, times(1)).existsByEmail(request.email());
        verify(userRepository, times(1)).existsByUsername(request.username());
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).userToUserDTO(newUser);
    }

    @Test
    void createUser_EmailAlreadyExists_ThrowsException() {
        NewUserRequest request = new NewUserRequest("NewUser", "password123!", "new_user@example.com");

        when(userRepository.existsByUsername(request.username())).thenReturn(false);
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(DataExistException.class)
                .hasMessageContaining("already exists");

        verify(userRepository, times(1)).existsByEmail(request.email());
        verify(userRepository, times(1)).existsByUsername(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

}