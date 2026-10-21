package com.posthub.util.model.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiLogMessage {
    CHANGED_JWT("Changed Jwt: '{}'"),
    SWAGGER_REQUEST("Request from Swagger."),
    REFERER_URL("URL from referer header: '{}'"),
    SERVER_PORT_FROM_REQUEST("'Server:Port', expected  '{}:{}', actual '{}:{}'"),
    INTERNAL_HEADER_FROM_REQUEST("Header '{}', expected value: '{}', actual: '{}'"),
    REQUEST_SERVER("Request's server {}:{}"),
    NAME_OF_CURRENT_METHOD("{} method executing."),
    SERVER_PORT_HEADER("Request's server {}:{}. Service Name Header: '{}'"),
    NAME_OF_CURRENT_METHOD_WITH_REQUEST("{} method executing. Request {}"),
    ACCESS_THROUGH_NOT_API_SERVICE("Unauthorized Access, access must be through the API service('{}')"),
    INNER_REQUEST("Request from Inner service.");

    private final String value;
}