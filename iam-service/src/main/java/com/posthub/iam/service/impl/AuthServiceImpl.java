package com.posthub.iam.service.impl;

import com.posthub.iam.mapper.UserMapper;
import com.posthub.iam.model.constants.ApiErrorMessage;
import com.posthub.iam.model.dto.user.LoginRequest;
import com.posthub.iam.model.dto.user.UserProfileDTO;
import com.posthub.iam.model.entity.RefreshToken;
import com.posthub.iam.model.entity.User;
import com.posthub.iam.model.exception.InvalidDataException;
import com.posthub.iam.model.responce.ApiResult;
import com.posthub.iam.repository.UserRepository;
import com.posthub.iam.security.JwtTokenProvider;
import com.posthub.iam.service.AuthService;
import com.posthub.iam.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public ApiResult<UserProfileDTO> login(@NonNull LoginRequest loginRequest) {

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

}
