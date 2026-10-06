package com.posthub.iam.service;

import com.posthub.iam.model.dto.user.LoginRequest;
import com.posthub.iam.model.dto.user.UserProfileDTO;
import com.posthub.iam.model.entity.RefreshToken;
import com.posthub.iam.model.responce.ApiResult;

public interface AuthService {

    ApiResult<UserProfileDTO> login(LoginRequest loginRequest);

    ApiResult<UserProfileDTO> refreshAccessToken(String refreshToken);

}
