package com.posthub.iam.service;

import com.posthub.iam.model.dto.user.UserDTO;
import com.posthub.iam.model.request.user.NewUserRequest;
import com.posthub.iam.model.responce.ApiResult;
import jakarta.validation.constraints.NotNull;

public interface UserService {

    ApiResult<UserDTO> getById(@NotNull Integer userId);

    ApiResult<UserDTO> createUser(@NotNull NewUserRequest newUserRequest);

}
