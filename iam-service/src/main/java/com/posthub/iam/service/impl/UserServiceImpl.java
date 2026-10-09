package com.posthub.iam.service.impl;

import com.posthub.iam.mapper.UserMapper;
import com.posthub.iam.model.constants.ApiErrorMessage;
import com.posthub.iam.model.dto.user.UserDTO;
import com.posthub.iam.model.dto.user.UserSearchDTO;
import com.posthub.iam.model.entity.Role;
import com.posthub.iam.model.entity.User;
import com.posthub.iam.model.exception.NotFoundException;
import com.posthub.iam.model.request.user.NewUserRequest;
import com.posthub.iam.model.request.user.UpdateUserRequest;
import com.posthub.iam.model.request.user.UserSearchRequest;
import com.posthub.iam.model.responce.ApiResult;
import com.posthub.iam.model.responce.PaginationResponse;
import com.posthub.iam.repository.RoleRepository;
import com.posthub.iam.repository.UserRepository;
import com.posthub.iam.repository.criteria.UserSearchCriteria;
import com.posthub.iam.security.validation.AccessValidator;
import com.posthub.iam.service.UserService;
import com.posthub.iam.service.model.IamServiceUserRole;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final AccessValidator accessValidator;

    @Override
    public ApiResult<UserDTO> getById(Integer userId) {
        User user = userRepository.findByIdAndDeletedFalseOrThrow(userId);

        UserDTO userDTO = userMapper.userToUserDTO(user);
        return ApiResult.createSuccessful(userDTO);
    }

    @Override
    public ApiResult<UserDTO> createUser(NewUserRequest newUserRequest) {

        userRepository.assertUsernameNotExists(newUserRequest.getUsername());

        userRepository.assertEmailNotExists(newUserRequest.getEmail());

        Role userRole = roleRepository.findByNameOrThrow(IamServiceUserRole.USER.getRole());

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
    @Transactional
    public ApiResult<UserDTO> updateUser(Integer userId, UpdateUserRequest updateUserRequest) {

        User user = userRepository.findByIdAndDeletedFalseOrThrow(userId);

        accessValidator.validateAdminOrOwnerAccess(user.getId());

        userRepository.assertUsernameNotExists(updateUserRequest.getUsername());

        userRepository.assertEmailNotExists(updateUserRequest.getEmail());

        userMapper.updateUser(user, updateUserRequest);
        user = userRepository.save(user);

        UserDTO userDTO = userMapper.userToUserDTO(user);
        return ApiResult.createSuccessful(userDTO);
    }

    @Override
    @Transactional
    public void softDeleteUser(Integer userId) {

        User user = userRepository.findByIdAndDeletedFalseOrThrow(userId);

        accessValidator.validateAdminOrOwnerAccess(user.getId());

        user.setDeleted(true);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return getUserDetails(email, userRepository);
    }

    static UserDetails getUserDetails(String email, UserRepository userRepository) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_WITH_ID_NOT_FOUND.format(email)));

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .toList()
        );
    }
}
