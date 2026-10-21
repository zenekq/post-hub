package com.posthub.util.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UtilsResponse<P> {

    private String message;
    private P payload;
    private boolean success;

    public static <P> UtilsResponse<P> create(String message, P payload, boolean success) {
        return new UtilsResponse<>(message, payload, success);
    }

    public static <P> UtilsResponse<P> createFailed(String message) {
        return new UtilsResponse<>(message, null, false);
    }

    public static <P> UtilsResponse<P> createFailed(String message, P payload) {
        return new UtilsResponse<>(message, payload, false);
    }

    public static <P> UtilsResponse<P> createSuccessful(P payload) {
        return new UtilsResponse<>("", payload, true);
    }

}