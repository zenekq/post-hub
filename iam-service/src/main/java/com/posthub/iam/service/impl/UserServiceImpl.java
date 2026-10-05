package com.posthub.iam.service.impl;

import com.posthub.iam.mapper.UserMapper;
import com.posthub.iam.model.constants.ApiErrorMassage;
import com.posthub.iam.model.dto.user.UserDTO;
import com.posthub.iam.model.dto.user.UserSearchDTO;
import com.posthub.iam.model.entity.Role;
import com.posthub.iam.model.entity.User;
import com.posthub.iam.model.exception.DataExistException;
import com.posthub.iam.model.exception.NotFoundException;
import com.posthub.iam.model.request.user.NewUserRequest;
import com.posthub.iam.model.request.user.UpdateUserRequest;
import com.posthub.iam.model.request.user.UserSearchRequest;
import com.posthub.iam.model.responce.ApiResult;
import com.posthub.iam.model.responce.PaginationResponse;
import com.posthub.iam.repository.RoleRepository;
import com.posthub.iam.repository.UserRepository;
import com.posthub.iam.repository.criteria.UserSearchCriteria;
import com.posthub.iam.service.UserService;
import com.posthub.iam.service.model.IamServiceUserRole;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@NullMarked
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public ApiResult<UserDTO> getById(Integer userId) {
        User user = userRepository.findByIdAndDeletedFalse(userId)
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

        Role userRole = roleRepository.findByName(IamServiceUserRole.USER.getRole())
                .orElseThrow(() -> new NotFoundException(ApiErrorMassage.USER_ROLE_NOT_FOUND.format(IamServiceUserRole.USER.getRole())));

        User user = userMapper.createUser(newUserRequest);
        user.setPassword(passwordEncoder.encode(newUserRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);
        UserDTO userDTO = userMapper.userToUserDTO(savedUser);

        return ApiResult.createSuccessful(userDTO);
    }

    @Override
    public ApiResult<UserDTO> updateUser(Integer userId, UpdateUserRequest updateUserRequest) {

        User user = userRepository.findByIdAndDeletedFalse(userId).orElseThrow(() ->
                new NotFoundException(ApiErrorMassage.USER_NOT_FOUND.format(userId)));

        if (userRepository.existsByUsername(updateUserRequest.getUsername())) {
            throw new DataExistException(ApiErrorMassage.USERNAME_ALREADY_EXIST.format(updateUserRequest.getUsername()));
        }

        if (userRepository.existsByEmail(updateUserRequest.getEmail())) {
            throw new DataExistException(ApiErrorMassage.EMAIL_ALREADY_EXIST.format(updateUserRequest.getEmail()));
        }

        userMapper.updateUser(user, updateUserRequest);
        user = userRepository.save(user);

        UserDTO userDTO = userMapper.userToUserDTO(user);
        return ApiResult.createSuccessful(userDTO);
    }

    @Override
    public void softDeleteUser(Integer userId) {

        User user = userRepository.findByIdAndDeletedFalse(userId).orElseThrow(() ->
                new NotFoundException(ApiErrorMassage.USER_NOT_FOUND.format(userId)));

        user.setDeleted(true);
        userRepository.save(user);
    }

    @Override
    public ApiResult<PaginationResponse<UserSearchDTO>> findAllUsers(Pageable pageable) {

        Page<UserSearchDTO> users = userRepository.findAll(pageable)
                .map(userMapper::toUserSearchDTO);

        PaginationResponse<UserSearchDTO> response = new PaginationResponse<>(
                users.getContent(),
                new PaginationResponse.Pagination(
                        users.getTotalElements(),
                        pageable.getPageSize(),
                        users.getNumber() + 1,
                        users.getTotalPages()
                )
        );

        return ApiResult.createSuccessful(response);
    }

    @Override
    public ApiResult<PaginationResponse<UserSearchDTO>> searchUsers(UserSearchRequest request, Pageable pageable) {

        Specification<User> userSearchCriteria = new UserSearchCriteria(request);

        Page<UserSearchDTO> users = userRepository.findAll(userSearchCriteria, pageable)
                .map(userMapper::toUserSearchDTO);

        PaginationResponse<UserSearchDTO> response = new PaginationResponse<>(
                users.getContent(),
                new PaginationResponse.Pagination(
                        users.getTotalElements(),
                        pageable.getPageSize(),
                        users.getNumber() + 1,
                        users.getTotalPages()
                )
        );

        return ApiResult.createSuccessful(response);
    }
}
