package com.gateway.model;

import lombok.Data;

@Data
public class GateWayRequest<T> {
    private String path;
    private String httpMethod;
    private T payload;

    public GateWayRequest(String path, String httpMethod, T payload) {
        this.path = path;
        this.httpMethod = httpMethod;
        this.payload = payload;
    }

    public static <T> GateWayRequest<T> create(String path, String httpMethod, T payload) {
        return new GateWayRequest<>(path, httpMethod, payload);
    }

}
