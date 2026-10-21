package com.posthub.util.model.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiConstants {
    public static final String UNDEFINED = "undefined";
    public static final String BREAK_LINE = "\n";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String DEFAULT_WHITESPACES_BEFORE_STACK_TRACE = "        ";
    public static final String SECURITY_PACKAGE_NAME = "org.springframework.security";
    public static final String DASH = "-";
}