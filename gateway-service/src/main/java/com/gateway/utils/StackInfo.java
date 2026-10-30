package com.gateway.utils;

import java.util.Iterator;

public final class StackInfo {

    private StackInfo() {}

    private static final StackWalker WALKER =
            StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    private static final String CURRENT_METHOD_LOG_TEMPLATE = "Current method: {}";
    private static final String CURRENT_METHOD_STRING_FORMAT = "Current method: %s";
    private static final String UNDEFINED = "undefined";

    public static String getMethodName() {

        try {
            return WALKER.walk(frames -> {
                Iterator<StackWalker.StackFrame> it = frames.iterator();
                it.next(); // skip this method name: getMethodName()
                return it.hasNext() ? it.next().getMethodName() : UNDEFINED;
            });

        } catch (Exception e) {
            return UNDEFINED;
        }
    }

    public static String getCurrentMethodLogTemplate() {
        return CURRENT_METHOD_LOG_TEMPLATE;
    }

    public static String getCurrentMethodString() {
        return CURRENT_METHOD_STRING_FORMAT;
    }

}
