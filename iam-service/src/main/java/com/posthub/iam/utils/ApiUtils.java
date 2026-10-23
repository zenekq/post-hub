package com.posthub.iam.utils;

import com.posthub.iam.security.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ApiUtils {

    private final JwtTokenProvider jwtTokenProvider;

    private static final StackWalker WALKER =
            StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    public static String getMethodName() {

        final String UNDEFINED = "undefined";
        try {
            return WALKER.walk(frames -> {
                Iterator<StackWalker.StackFrame> it = frames.iterator();
                it.next(); // skip this method name: getMethodName
                return it.hasNext() ? it.next().getMethodName() : UNDEFINED;
            });

        } catch (Exception e) {
            return UNDEFINED;
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

    public Long getUserIdFromAuthentication() {
        String jwtToken = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        return Long.parseLong(jwtTokenProvider.getUserId(jwtToken));
    }

}
