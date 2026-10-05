package com.posthub.iam.model.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiMessage {

    TOKEN_CREATED_OR_UPDATED("User token has been created or updated"),
    ;

    private final String message;

}
