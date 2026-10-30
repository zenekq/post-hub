package com.gateway.model.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiLogMessage {
    RESPONSE_BODY("Response body:\n{}"),
    SERVICE_RESPONSE_IS_FAILED("{} response is failed with message '{}'."),
    IAM_FEIGN_BODY("IamFeign received body: {}"),
    TOKEN_IS_VALID("Token is VALID"),
    TOKEN_IS_INVALID("Token is INVALID"),
    EMPTY_AUTHORIZE_HEADERS("EMPTY AUTHORIZATION HEADERS"),
    INVALID_AUTHORIZE_HEADER("INVALID AUTHORIZATION HEADER"),
    URL_IN_AUTH("ServiceAuthFilter: {} uri: {}"),
    URL_IN_REQUEST("RequestFilter: uri: {}"),
    URL_IN_RESPONSE("LoginResponseFilter: uri = {}"),
    BAD_CREDENTIAL("Bad credentials: {}"),
    SUCCEED_AUTHENTICATION("Authentication was succeed: {}"),
    AUTH_FILTER_ERROR("Auth filter error: {}"),
    NAME_OF_CURRENT_METHOD("{} method executing. "),
    KEY_VALID("Header key is VALID. "),
    KEY_NOT_VALID("Header key is invalid. ");

    private final String message;

}
