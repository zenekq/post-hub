package com.gateway.model.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum ApiMessage {
    ACCESS_TO_SERVICE_NOT_GRANTED("Access to %s is not granted"),
    ACCESS_NOT_GRANTED("Access is not granted");

    private final String message;

    public String getMessage(String arg1) {
        return String.format(message, Objects.nonNull(arg1) ? arg1 : "null");
    }

}
