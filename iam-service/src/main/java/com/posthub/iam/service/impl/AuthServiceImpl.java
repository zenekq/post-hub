package com.posthub.iam.service.impl;

import com.posthub.iam.mapper.UserMapper;
import com.posthub.iam.model.constants.ApiErrorMessage;
import com.posthub.iam.model.dto.user.LoginRequest;
import com.posthub.iam.model.dto.user.UserProfileDTO;
import com.posthub.iam.model.entity.RefreshToken;
import com.posthub.iam.model.entity.Role;
import com.posthub.iam.model.entity.User;
import com.posthub.iam.model.exception.DataExistException;
import com.posthub.iam.model.exception.InvalidDataException;
import com.posthub.iam.model.exception.NotFoundException;
import com.posthub.iam.model.request.user.RegistrationUserRequest;
import com.posthub.iam.model.responce.ApiResult;
import com.posthub.iam.repository.RoleRepository;
import com.posthub.iam.repository.UserRepository;
import com.posthub.iam.security.JwtTokenProvider;
import com.posthub.iam.service.AuthService;
import com.posthub.iam.service.RefreshTokenService;
import com.posthub.iam.service.model.IamServiceUserRole;
import com.posthub.iam.utils.PasswordUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
@NullMarked
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public ApiResult<UserProfileDTO> login(LoginRequest loginRequest) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new InvalidDataException(ApiErrorMessage.INVALID_USER_OR_PASSWORD.getValue());

        }

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new InvalidDataException(ApiErrorMessage.INVALID_USER_OR_PASSWORD.getValue()));

        String token = jwtTokenProvider.generateToken(user);

        RefreshToken refreshToken = refreshTokenService.generateOrUpdateRefreshToken(user);
        UserProfileDTO userProfileDTO = userMapper.toUserProfileDTO(user, token, refreshToken.getToken());
        userProfileDTO.setToken(token);

        return ApiResult.createSuccessfulWithNewToken(userProfileDTO);
    }

    @Override
    @Transactional
    public ApiResult<UserProfileDTO> refreshAccessToken(String refreshTokenValue) {

        RefreshToken refreshToken = refreshTokenService.validateAndRefreshToken(refreshTokenValue);
        User user = refreshToken.getUser();

        String accessToken = jwtTokenProvider.generateToken(user);


        return ApiResult.createSuccessfulWithNewToken(userMapper.toUserProfileDTO(user, accessToken, refreshToken.getToken()));
    }

    @Override
    @Transactional
    public ApiResult<UserProfileDTO> registerUser(RegistrationUserRequest request) {

        userRepository.findByUsername(request.getUsername())
            .ifPresent(existingUser -> {
                throw new DataExistException(ApiErrorMessage.USERNAME_ALREADY_EXIST.format(request.getUsername()));
            });

        userRepository.findByEmail(request.getEmail())
            .ifPresent(existingUser -> {
                throw new DataExistException(ApiErrorMessage.EMAIL_ALREADY_EXIST.format(request.getEmail()));
            });

        String password = request.getPassword();
        String confirmPassword = request.getConfirmPassword();

        if (!password.equals(confirmPassword)) {
            throw new InvalidDataException(ApiErrorMessage.MISMATCH_PASSWORDS.getValue());
        }

        if (PasswordUtils.isNotValidPassword(password)) {
            throw new InvalidDataException(ApiErrorMessage.INVALID_PASSWORD.getValue());
        }


        String stringRole = IamServiceUserRole.USER.getRole();
        Role role = roleRepository.findByName(stringRole)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_ROLE_NOT_FOUND.format(stringRole)));

        User newUser = userMapper.fromDto(request);
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        newUser.setRoles(roles);
        userRepository.save(newUser);

        RefreshToken refreshToken = refreshTokenService.generateOrUpdateRefreshToken(newUser);
        String token = jwtTokenProvider.generateToken(newUser);

        UserProfileDTO userProfileDTO = userMapper.toUserProfileDTO(newUser, token, refreshToken.getToken());
        userProfileDTO.setToken(token);

        return ApiResult.createSuccessfulWithNewToken(userProfileDTO);
    }

}
