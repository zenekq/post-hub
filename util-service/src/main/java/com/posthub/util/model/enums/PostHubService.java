package com.posthub.util.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
@AllArgsConstructor()
public enum PostHubService {
    GATEWAY_SERVICE("gateway-service"),
    IAM_SERVICE("iam-service"),
    UTILS_SERVICE("utils-service"),
    UNDEFINED_SERVICE("Undefined-service");

    private final String value;

    public static PostHubService fromValue(String searchValue) {
        return Arrays.stream(PostHubService.values())
                .filter(v -> Objects.equals(v.value, searchValue))
                .findFirst()
                .orElse(PostHubService.UNDEFINED_SERVICE);
    }
}
