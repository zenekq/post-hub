package com.posthub.iam.service;


import com.posthub.iam.mapper.UserMapper;
import com.posthub.iam.model.dto.user.UserDTO;
import com.posthub.iam.model.entity.Role;
import com.posthub.iam.model.entity.User;
import com.posthub.iam.repository.RoleRepository;
import com.posthub.iam.repository.UserRepository;
import com.posthub.iam.service.impl.UserServiceImpl;
import com.posthub.iam.service.model.IamServiceUserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserDTO testUserDTO;
    private Role superAdminRole;

    private static int userId = 1;

    @BeforeEach
    void setUp() {
        superAdminRole = new Role();
        superAdminRole.setName(IamServiceUserRole.SUPER_ADMIN.getRole());

        testUser = new User();
        testUser.setId(userId);
        testUser.setUsername("TestUser");
        testUser.setEmail("TestUser@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setRoles(Set.of(superAdminRole));

        testUserDTO = new UserDTO();
        testUserDTO.setId(testUser.getId());
        testUserDTO.setUsername(testUser.getUsername());
        testUserDTO.setEmail(testUser.getEmail());
    }

    @Test
    void getById_UserExists_ReturnsUserDTO() {
        when(userRepository.findByIdAndDeletedFalseOrThrow(userId)).thenReturn(testUser);
        when(userMapper.userToUserDTO(testUser)).thenReturn(testUserDTO);

        UserDTO result = userService.getById(userId).getPayload();

        assertNotNull(result);
        assertEquals(testUserDTO.getId(), result.getId());
        assertEquals(testUserDTO.getUsername(), result.getUsername());
        assertEquals(testUserDTO.getEmail(), result.getEmail());

        verify(userRepository, times(1)).findByIdAndDeletedFalseOrThrow(1);
        verify(userMapper, times(1)).userToUserDTO(testUser);
    }

}