package com.gateway.model.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiConstants {
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String ACCESS_KEY_HEADER_NAME = "key";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String DEFAULT_WHITESPACES_BEFORE_STACK_TRACE = "        ";
    public static final String BREAK_LINE = "\n";
    public static final String DEFAULT_PACKAGE_NAME = "com.post_hub.gateway";
    public static final String UNDEFINED = "undefined";

}
