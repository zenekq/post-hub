package com.posthub.iam.model.responce;

import com.posthub.iam.model.constants.ApiMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResult<P> {
    private String message;
    private P payload;
    private boolean success;

    private static final String EMPTY_STRING = "";

    public static <P> ApiResult<P> createSuccessful(P payload) {
        return new ApiResult<>(EMPTY_STRING, payload, true);
    }

    public static <P> ApiResult<P> createSuccessfulWithNewToken(P payload) {
        return new ApiResult<>(ApiMessage.TOKEN_CREATED_OR_UPDATED.getMessage(), payload, true);
    }

}
