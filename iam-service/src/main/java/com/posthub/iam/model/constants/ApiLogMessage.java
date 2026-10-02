package com.posthub.iam.model.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiLogMessage {

    POST_INFO_BY_ID("Receiving post with ID: {}"),
    NAME_OF_CURRENT_METHOD("Current method: {}"),
    ;

    private final String value;
    private final String formatTemplate;

    ApiLogMessage(String value) {
        this.value = value;
        this.formatTemplate = value.replace("{}", "%s");
    }

    public String format(Object... args) {
        return String.format(formatTemplate, args);
    }

}
