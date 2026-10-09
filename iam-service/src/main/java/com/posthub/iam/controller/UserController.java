package com.posthub.iam.controller;

import com.posthub.iam.model.constants.ApiLogMessage;
import com.posthub.iam.model.dto.user.UserDTO;
import com.posthub.iam.model.dto.user.UserSearchDTO;
import com.posthub.iam.model.request.user.NewUserRequest;
import com.posthub.iam.model.request.user.UpdateUserRequest;
import com.posthub.iam.model.request.user.UserSearchRequest;
import com.posthub.iam.model.responce.ApiResult;
import com.posthub.iam.model.responce.PaginationResponse;
import com.posthub.iam.service.UserService;
import com.posthub.iam.utils.ApiUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("${end.points.users}")
public class UserController {

    private final UserService userService;

    @GetMapping("${end.points.id}")
    public ResponseEntity<ApiResult<UserDTO>> getUserById(
            @PathVariable(name = "id") Integer userId) {

        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        ApiResult<UserDTO> response = userService.getById(userId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("${end.points.create}")
    public ResponseEntity<ApiResult<UserDTO>> createUser(
            @Valid @RequestBody NewUserRequest newUserRequest) {

        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        ApiResult<UserDTO> user = userService.createUser(newUserRequest);

        return ResponseEntity.ok(user);
    }

    @PutMapping("${end.points.id}")
    public ResponseEntity<ApiResult<UserDTO>> updateUserById(
            @PathVariable(name = "id") Integer userId,
            @RequestBody @Valid UpdateUserRequest updateUserRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        ApiResult<UserDTO> response = userService.updateUser(userId,updateUserRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("${end.points.id}")
    public ResponseEntity<Void> softDeleteUser(
            @PathVariable(name = "id") Integer userId) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        userService.softDeleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("${end.points.all}")
    public ResponseEntity<ApiResult<PaginationResponse<UserSearchDTO>>> getAllUsers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        Pageable pageable = PageRequest.of(page, limit);
        ApiResult<PaginationResponse<UserSearchDTO>> response = userService.findAllUsers(pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("${end.points.search}")
    public ResponseEntity<ApiResult<PaginationResponse<UserSearchDTO>>> searchUsers(
            @RequestBody @Valid UserSearchRequest request,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        Pageable pageable = PageRequest.of(page, limit);
        ApiResult<PaginationResponse<UserSearchDTO>> response = userService.searchUsers(request, pageable);
        return ResponseEntity.ok(response);
    }


}
