package com.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GateWayResponse<T> {
    private String message;
    private T payload;
    private boolean success;

    public static <T > GateWayResponse<T> createFailed(String message) {
        return new GateWayResponse<>(message, null, false);
    }

    @Override
    public String toString() {
        return "IamResponse{" +
                "Success=" + success +
                ", message='" + message + '\'' +
                ", body='" + payload + '\'' +
                '}';
    }

    public boolean isFailed() {
        return !success;
    }

}
