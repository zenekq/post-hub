package com.posthub.iam.service;

import com.posthub.iam.mapper.UserMapper;
import com.posthub.iam.model.dto.user.UserProfileDTO;
import com.posthub.iam.model.entity.RefreshToken;
import com.posthub.iam.model.entity.Role;
import com.posthub.iam.model.entity.User;
import com.posthub.iam.model.enums.RegistrationStatus;
import com.posthub.iam.repository.RoleRepository;
import com.posthub.iam.repository.UserRepository;
import com.posthub.iam.security.JwtTokenProvider;
import com.posthub.iam.security.validation.AccessValidator;
import com.posthub.iam.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    RefreshTokenService refreshTokenService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private Argon2PasswordEncoder passwordEncoder;

    @Mock
    AccessValidator accessValidator;

    @InjectMocks
    private AuthServiceImpl authService;

    private User testUser;
    private UserProfileDTO testUserProfileDTO;
    private RefreshToken testRefreshToken;
    private Role userRole;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("TestUser");
        testUser.setEmail("test_user@example.com");
        testUser.setPassword("hashedPassword");
        testUser.setRegistrationStatus(RegistrationStatus.ACTIVE);
        testUser.setLastLogin(LocalDateTime.now());

        userRole = new Role();
        userRole.setName("USER");
        testUser.setRoles(Collections.singleton(userRole));

        testRefreshToken  = new RefreshToken();
        testRefreshToken.setToken("refresh_token_123");
        testRefreshToken.setUser(testUser);

        testUserProfileDTO = new UserProfileDTO(
                testUser.getId(),
                testUser.getUsername(),
                testUser.getEmail(),
                testUser.getRegistrationStatus(),
                testUser.getLastLogin(),
                "access_token_123",
                testRefreshToken.getToken(),
                Collections.emptyList()
        );
    }

}
