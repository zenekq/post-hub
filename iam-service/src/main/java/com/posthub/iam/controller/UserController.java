package com.posthub.iam.controller;

import com.posthub.iam.model.dto.user.UserDTO;
import com.posthub.iam.model.dto.user.UserSearchDTO;
import com.posthub.iam.model.request.pagination.PaginationRequest;
import com.posthub.iam.model.request.user.NewUserRequest;
import com.posthub.iam.model.request.user.UpdateUserRequest;
import com.posthub.iam.model.request.user.UserSearchRequest;
import com.posthub.iam.model.response.ApiResult;
import com.posthub.iam.model.response.PaginationResponse;
import com.posthub.iam.service.UserService;
import com.posthub.iam.utils.StackInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.controller.UserController.logLevel:TRACE}")
    private Level logLevel;

    @GetMapping("${end.points.id}")
    public ResponseEntity<ApiResult<UserDTO>> getUserById(
            @PathVariable(name = "id") Long userId) {

        if (log.isEnabledForLevel(logLevel)) log.atLevel(logLevel).log(StackInfo.getMethodInfo());

        ApiResult<UserDTO> response = userService.getById(userId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("${end.points.create}")
    public ResponseEntity<ApiResult<UserDTO>> createUser(
            @Valid @RequestBody NewUserRequest newUserRequest) {

        if (log.isEnabledForLevel(logLevel)) log.atLevel(logLevel).log(StackInfo.getMethodInfo());

        ApiResult<UserDTO> user = userService.createUser(newUserRequest);

        return ResponseEntity.ok(user);
    }

    @PutMapping("${end.points.id}")
    public ResponseEntity<ApiResult<UserDTO>> updateUserById(
            @PathVariable(name = "id") Long userId,
            @RequestBody @Valid UpdateUserRequest updateUserRequest) {

        if (log.isEnabledForLevel(logLevel)) log.atLevel(logLevel).log(StackInfo.getMethodInfo());

        ApiResult<UserDTO> response = userService.updateUser(userId,updateUserRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("${end.points.id}")
    public ResponseEntity<Void> softDeleteUser(
            @PathVariable(name = "id") Long userId) {

        if (log.isEnabledForLevel(logLevel)) log.atLevel(logLevel).log(StackInfo.getMethodInfo());

        userService.softDeleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("${end.points.all}")
    public ResponseEntity<ApiResult<PaginationResponse<UserSearchDTO>>> getAllUsers(
            @ModelAttribute PaginationRequest paginationRequest) {

        if (log.isEnabledForLevel(logLevel)) log.atLevel(logLevel).log(StackInfo.getMethodInfo());

        ApiResult<PaginationResponse<UserSearchDTO>> response = userService.findAllUsers(paginationRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("${end.points.search}")
    public ResponseEntity<ApiResult<PaginationResponse<UserSearchDTO>>> searchUsers(
            @RequestBody @Valid UserSearchRequest request,
            @ModelAttribute PaginationRequest paginationRequest) {

        if (log.isEnabledForLevel(logLevel)) log.atLevel(logLevel).log(StackInfo.getMethodInfo());

        ApiResult<PaginationResponse<UserSearchDTO>> response = userService.searchUsers(request, paginationRequest);
        return ResponseEntity.ok(response);
    }

}
