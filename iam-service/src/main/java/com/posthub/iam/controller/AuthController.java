package com.posthub.iam.controller;

import com.posthub.iam.model.constants.ApiLogMessage;
import com.posthub.iam.model.dto.user.LoginRequest;
import com.posthub.iam.model.dto.user.UserProfileDTO;
import com.posthub.iam.model.responce.ApiResult;
import com.posthub.iam.service.AuthService;
import com.posthub.iam.utils.ApiUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${end.point.auth}")
public class AuthController {

    private final AuthService authService;

    @PostMapping("${end.point.login}")
    public ResponseEntity<?> login(
            @RequestBody @Valid LoginRequest loginRequest,
            HttpServletResponse response) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        ApiResult<UserProfileDTO> result = authService.login(loginRequest);
        Cookie authCookie = ApiUtils.createAuthCookie(result.getPayload().getToken());
        response.addCookie(authCookie);

        return ResponseEntity.ok(result);
    }

}
