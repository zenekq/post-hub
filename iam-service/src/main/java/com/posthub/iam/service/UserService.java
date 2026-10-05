package com.posthub.iam.service;

import com.posthub.iam.model.dto.user.UserDTO;
import com.posthub.iam.model.dto.user.UserSearchDTO;
import com.posthub.iam.model.request.user.NewUserRequest;
import com.posthub.iam.model.request.user.UpdateUserRequest;
import com.posthub.iam.model.request.user.UserSearchRequest;
import com.posthub.iam.model.responce.ApiResult;
import com.posthub.iam.model.responce.PaginationResponse;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

@NullMarked
public interface UserService extends UserDetailsService {

    ApiResult<UserDTO> getById(Integer userId);

    ApiResult<UserDTO> createUser(NewUserRequest newUserRequest);

    ApiResult<UserDTO> updateUser(Integer userId, UpdateUserRequest request);

    void softDeleteUser(Integer userId);

    ApiResult<PaginationResponse<UserSearchDTO>> findAllUsers(Pageable pageable);

    ApiResult<PaginationResponse<UserSearchDTO>> searchUsers(UserSearchRequest request, Pageable pageable);

}
