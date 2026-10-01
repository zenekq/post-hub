package com.posthub.iam.model.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiErrorMassage {
    POST_NOT_FOUND_BY_ID("Post with ID: {} was not found"),
    POST_ALREADY_EXIST("Post with Title: {} already exists"),
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
