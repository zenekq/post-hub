package com.posthub.iam.kafka.model.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostHubService {
    GATEWAY_SERVICE("gateway-service"),
    IAM_SERVICE("iam-service"),
    UTILS_SERVICE("utils-service"),
    UNDEFINED_SERVICE("Undefined-service");

    private final String value;
}
