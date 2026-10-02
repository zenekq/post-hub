package com.posthub.iam.service.impl;

import com.posthub.iam.mapper.UserMapper;
import com.posthub.iam.model.constants.ApiErrorMassage;
import com.posthub.iam.model.constants.ApiLogMessage;
import com.posthub.iam.model.dto.user.UserDTO;
import com.posthub.iam.model.entity.User;
import com.posthub.iam.model.exception.DataExistException;
import com.posthub.iam.model.exception.NotFoundException;
import com.posthub.iam.model.request.user.NewUserRequest;
import com.posthub.iam.model.responce.ApiResult;
import com.posthub.iam.repository.UserRepository;
import com.posthub.iam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public ApiResult<UserDTO> getById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ApiErrorMassage.USER_NOT_FOUND.format(userId)));

        UserDTO userDTO = userMapper.userToUserDTO(user);
        return ApiResult.createSuccessful(userDTO);
    }

    @Override
    public ApiResult<UserDTO> createUser(NewUserRequest newUserRequest) {

        if (userRepository.existsByUsername(newUserRequest.getUsername())) {
            throw new DataExistException(ApiErrorMassage.USERNAME_ALREADY_EXIST.format(newUserRequest.getUsername()));
        }

        if (userRepository.existsByEmail(newUserRequest.getEmail())) {
            throw new DataExistException(ApiErrorMassage.EMAIL_ALREADY_EXIST.format(newUserRequest.getEmail()));
        }

        User user = userMapper.createUser(newUserRequest);
        User savedUser = userRepository.save(user);
        UserDTO userDTO = userMapper.userToUserDTO(savedUser);

        return ApiResult.createSuccessful(userDTO);
    }
}
