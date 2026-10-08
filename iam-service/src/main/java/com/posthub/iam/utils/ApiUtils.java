package com.posthub.iam.utils;

import com.posthub.iam.model.constants.ApiConstants;
import com.posthub.iam.security.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ApiUtils {

    private final JwtTokenProvider jwtTokenProvider;

    private static final StackWalker WALKER =
            StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    public static String getMethodName() {
        try {
            return WALKER.walk(frames ->
                    frames.skip(1).findFirst().map(StackWalker.StackFrame::getMethodName)
                            .orElse(ApiConstants.UNDEFINED)
            );
        } catch (Exception e) {
            return ApiConstants.UNDEFINED;
        }
    }

    public static Cookie createAuthCookie(String value) {
        Cookie authorizationCookie = new Cookie(HttpHeaders.AUTHORIZATION, value);
        authorizationCookie.setHttpOnly(true);
        authorizationCookie.setSecure(true);
        authorizationCookie.setPath("/");
        authorizationCookie.setMaxAge(300);
        return authorizationCookie;
    }

    public static String generateUidWithoutDash() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getCurrentUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public Integer getUserIdFromAuthentication() {
        String jwtToken = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        return Integer.parseInt(jwtTokenProvider.getUserId(jwtToken));
    }

}
