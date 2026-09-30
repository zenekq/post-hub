package com.posthub.iam.model.responce;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResult<P extends Serializable> implements Serializable {
    private String message;
    private P payload;
    private boolean success;

    private static final String EMPTY_STRING = "";

    public static <P extends Serializable> ApiResult<P> createSuccessful(P payload) {
        return new ApiResult<>(EMPTY_STRING, payload, true);
    }
}
