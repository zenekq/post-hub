package com.posthub.iam.controller;

import com.posthub.iam.model.constants.ApiLogMessage;
import com.posthub.iam.model.dto.user.LoginRequest;
import com.posthub.iam.model.dto.user.UserProfileDTO;
import com.posthub.iam.model.request.user.RegistrationUserRequest;
import com.posthub.iam.model.responce.ApiResult;
import com.posthub.iam.service.AuthService;
import com.posthub.iam.utils.ApiUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${end.points.auth}")
public class AuthController {

    private final AuthService authService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful authorization",
            content = @Content(mediaType = "applicatio/json",
                    examples = @ExampleObject(value = "{\"token\": \"eyJhbGciOiJIUz\"} ")))

    })
    @PostMapping("${end.points.login}")
    @Operation(
            summary = "User login",
            description = "Authenticates the user and returns an access/refresh token"
    )
    public ResponseEntity<ApiResult<UserProfileDTO>> login(
            @RequestBody @Valid LoginRequest loginRequest,
            HttpServletResponse response) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        ApiResult<UserProfileDTO> result = authService.login(loginRequest);
        Cookie authCookie = ApiUtils.createAuthCookie(result.getPayload().getToken());
        response.addCookie(authCookie);

        return ResponseEntity.ok(result);
    }

    @GetMapping("${end.points.refresh.token}")
    @Operation(
            summary = "Refresh access token",
            description = "Generate new access token using provided refresh token"
    )
    public ResponseEntity<ApiResult<UserProfileDTO>> refreshToken(
          @RequestParam(name = "token") String refreshToken,
          HttpServletResponse response) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        ApiResult<UserProfileDTO> result = authService.refreshAccessToken(refreshToken);
        Cookie authCookie = ApiUtils.createAuthCookie(result.getPayload().getToken());
        response.addCookie(authCookie);

        return  ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Register a new user",
            description = "Create new user and return authentication details"
    )
    @PostMapping("${end.points.register}")
    ResponseEntity<ApiResult<UserProfileDTO>> register(
            @RequestBody @Valid RegistrationUserRequest request,
            HttpServletResponse response) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        ApiResult<UserProfileDTO> result = authService.registerUser(request);
        Cookie authCookie = ApiUtils.createAuthCookie(result.getPayload().getToken());
        response.addCookie(authCookie);

        return ResponseEntity.ok(result);
    }

}
