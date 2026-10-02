package com.posthub.iam.model.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiErrorMassage {
    POST_NOT_FOUND_BY_ID("Post with ID: {} was not found"),
    POST_ALREADY_EXIST("Post with Title: {} already exists"),
    USER_NOT_FOUND("User not found with ID: {}"),
    USERNAME_ALREADY_EXIST("Username: {} already exists"),
    EMAIL_ALREADY_EXIST("Email: {} already exists"),
    ;

    private final String value;
    private final String formatTemplate;

    ApiErrorMassage(String value) {
        this.value = value;
        this.formatTemplate = value.replace("{}", "%s");
    }

    public String format(Object... args) {
        return String.format(formatTemplate, args);
    }
}
