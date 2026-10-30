package com.posthub.iam.controller;

import com.posthub.iam.model.dto.user.UserDTO;
import com.posthub.iam.model.dto.user.UserSearchDTO;
import com.posthub.iam.model.request.pagination.PaginationRequest;
import com.posthub.iam.model.request.user.NewUserRequest;
import com.posthub.iam.model.request.user.UpdateUserRequest;
import com.posthub.iam.model.request.user.UserSearchRequest;
import com.posthub.iam.model.responce.ApiResult;
import com.posthub.iam.model.responce.PaginationResponse;
import com.posthub.iam.service.UserService;
import com.posthub.iam.utils.StackInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            @PathVariable(name = "id") Long userId) {

        log.trace(StackInfo.getCurrentMethodLogTemplate(), StackInfo.getMethodName());

        ApiResult<UserDTO> response = userService.getById(userId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("${end.points.create}")
    public ResponseEntity<ApiResult<UserDTO>> createUser(
            @Valid @RequestBody NewUserRequest newUserRequest) {

        log.trace(StackInfo.getCurrentMethodLogTemplate(), StackInfo.getMethodName());

        ApiResult<UserDTO> user = userService.createUser(newUserRequest);

        return ResponseEntity.ok(user);
    }

    @PutMapping("${end.points.id}")
    public ResponseEntity<ApiResult<UserDTO>> updateUserById(
            @PathVariable(name = "id") Long userId,
            @RequestBody @Valid UpdateUserRequest updateUserRequest) {
        log.trace(StackInfo.getCurrentMethodLogTemplate(), StackInfo.getMethodName());

        ApiResult<UserDTO> response = userService.updateUser(userId,updateUserRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("${end.points.id}")
    public ResponseEntity<Void> softDeleteUser(
            @PathVariable(name = "id") Long userId) {

        log.trace(StackInfo.getCurrentMethodLogTemplate(), StackInfo.getMethodName());

        userService.softDeleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("${end.points.all}")
    public ResponseEntity<ApiResult<PaginationResponse<UserSearchDTO>>> getAllUsers(
            @ModelAttribute PaginationRequest paginationRequest) {

        log.trace(StackInfo.getCurrentMethodLogTemplate(), StackInfo.getMethodName());

        ApiResult<PaginationResponse<UserSearchDTO>> response = userService.findAllUsers(paginationRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("${end.points.search}")
    public ResponseEntity<ApiResult<PaginationResponse<UserSearchDTO>>> searchUsers(
            @RequestBody @Valid UserSearchRequest request,
            @ModelAttribute PaginationRequest paginationRequest) {

        log.trace(StackInfo.getCurrentMethodLogTemplate(), StackInfo.getMethodName());

        ApiResult<PaginationResponse<UserSearchDTO>> response = userService.searchUsers(request, paginationRequest);
        return ResponseEntity.ok(response);
    }

}
