package com.posthub.iam.service.impl;

import com.posthub.iam.model.constants.ApiErrorMessage;
import com.posthub.iam.model.entity.RefreshToken;
import com.posthub.iam.model.entity.User;
import com.posthub.iam.model.exception.NotFoundException;
import com.posthub.iam.repository.RefreshTokenRepository;
import com.posthub.iam.service.RefreshTokenService;
import com.posthub.iam.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken generateOrUpdateRefreshToken(User user) {
        return refreshTokenRepository.findByUserId(user.getId())
                .map(refreshToken -> {
                    refreshToken.setCreated(LocalDateTime.now());
                    refreshToken.setToken(ApiUtils.generateUidWithoutDash());
                    return refreshTokenRepository.save(refreshToken);
                }).orElseGet(() -> {
                      RefreshToken newToken = new RefreshToken();
                      newToken.setUser(user);
                      newToken.setCreated(LocalDateTime.now());
                      newToken.setToken(ApiUtils.generateUidWithoutDash());
                      return refreshTokenRepository.save(newToken);
                });
    }

    @Override
    public RefreshToken validateAndRefreshToken(String requestRefreshToken) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(requestRefreshToken)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.NOT_FOUND_REFRESH_TOKEN.getValue()));

        refreshToken.setCreated(LocalDateTime.now());
        refreshToken.setToken(ApiUtils.generateUidWithoutDash());

        return refreshTokenRepository.save(refreshToken);
    }

}
