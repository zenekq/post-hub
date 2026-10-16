package com.posthub.iam.service;

import com.posthub.iam.mapper.UserMapper;
import com.posthub.iam.model.dto.user.LoginRequest;
import com.posthub.iam.model.dto.user.UserProfileDTO;
import com.posthub.iam.model.entity.RefreshToken;
import com.posthub.iam.model.entity.Role;
import com.posthub.iam.model.entity.User;
import com.posthub.iam.model.enums.RegistrationStatus;
import com.posthub.iam.model.exception.InvalidDataException;
import com.posthub.iam.model.request.user.RegistrationUserRequest;
import com.posthub.iam.repository.RoleRepository;
import com.posthub.iam.repository.UserRepository;
import com.posthub.iam.security.JwtTokenProvider;
import com.posthub.iam.security.validation.AccessValidator;
import com.posthub.iam.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    void login_ValidCredentials_ReturnsUserProfile() {

        LoginRequest request = new LoginRequest(testUser.getEmail(), testUser.getPassword());
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(testUser));
        when(jwtTokenProvider.generateToken(testUser)).thenReturn("access_token_123");
        when(refreshTokenService.generateOrUpdateRefreshToken(testUser)).thenReturn(testRefreshToken);
        when(userMapper.toUserProfileDTO(testUser, "access_token_123", testRefreshToken.getToken()))
                .thenReturn(testUserProfileDTO);

        UserProfileDTO result = authService.login(request).getPayload();

        assertNotNull(result);
        assertEquals(testUserProfileDTO, result);
    }

    @Test
    void login_InvalidCredentials_ThrowsException() {

        LoginRequest request = new LoginRequest("test@example.com", "wrongPassword");
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        InvalidDataException ex = assertThrows(InvalidDataException.class,
                () -> authService.login(request));

        assertTrue(ex.getMessage().contains("Invalid"));
    }

    @Test
    void registerUser_ValidRequest_CreatesUserSuccessfully() {

        RegistrationUserRequest request = new RegistrationUserRequest(
                "newUser", "test@example.com", "password123!", "password123!"
        );

        doNothing().when(accessValidator).validateNewUser(
                anyString(),
                anyString(),
                anyString(),
                anyString()
        );

        when(roleRepository.findByNameOrThrow(any())).thenReturn(userRole);
        when(userMapper.fromDto(request)).thenReturn(testUser);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(refreshTokenService.generateOrUpdateRefreshToken(testUser)).thenReturn(testRefreshToken);
        when(jwtTokenProvider.generateToken(testUser)).thenReturn("access_token_123");
        when(userMapper.toUserProfileDTO(testUser, "access_token_123", testRefreshToken.getToken()))
                .thenReturn(testUserProfileDTO);

        UserProfileDTO result = authService.registerUser(request).getPayload();

        assertNotNull(result);
        assertEquals(testUserProfileDTO, result);
    }

}
