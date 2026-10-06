package com.posthub.iam.service;

import com.posthub.iam.model.entity.RefreshToken;
import com.posthub.iam.model.entity.User;

public interface RefreshTokenService {

   RefreshToken generateOrUpdateRefreshToken(User user);

   RefreshToken validateAndRefreshToken(String refreshToken);

}
