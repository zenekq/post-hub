package com.posthub.util.model.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiErrorMessage {
    INVALID_TOKEN_PROVIDED("Invalid token provided"),
    INVALID_EMAIL("Invalid email"),
    USER_ACCESS_DENIED("User {} with tenantId {} doesn't have access to requested tenantId {} resource"),
    NOT_FOUND_LOG_USER("LogId: {} for userId: {} is not found in DB"),
    NOT_FOUND_ACTION_LOG("LogId: {} was not found"),
    NOT_FOUND_ACTION_LOG_FOR_USER("LogId: {} for userId: {} was not found"),
    EMPTY_USER_ID("User ID must not be null"),
    ACCESS_DENIED("Access denied: You can only view your own logs."),
    ACCESS_THROUGH_NOT_API_SERVICE("Unauthorized Access, you should pass through the API service");

    private final String message;
    private final String formatTemplate;

    ApiErrorMessage(String message) {
        this.message = message;
        this.formatTemplate = message.replace("{}", "'%s'");
    }

    public String format(Object... args) {
        return String.format(formatTemplate, args);
    }
}
