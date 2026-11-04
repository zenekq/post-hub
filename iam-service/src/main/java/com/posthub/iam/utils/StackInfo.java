package com.posthub.iam.utils;

import java.util.Iterator;

public final class StackInfo {

    private StackInfo() {}

    private static final StackWalker WALKER =
            StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    private static final String CURRENT_METHOD_LOG = "Current method: ";
    private static final String CURRENT_METHOD_LOG_TEMPLATE = CURRENT_METHOD_LOG + "{}";
    private static final String CURRENT_METHOD_STRING_FORMAT = CURRENT_METHOD_LOG + "%s";
    private static final String UNDEFINED = "undefined";

    public static String getMethodName() {

        return WALKER.walk(frames -> {
            Iterator<StackWalker.StackFrame> it = frames.iterator();
            it.next(); // skip this method name: getMethodName()
            return it.hasNext() ? it.next().getMethodName() : UNDEFINED;
        });
    }

    public static String getCurrentMethod() {

        return WALKER.walk(frames -> {
            Iterator<StackWalker.StackFrame> it = frames.iterator();
            it.next(); // skip this method name: getCurrentMethod()
            String methodName = it.hasNext() ? it.next().getMethodName() : UNDEFINED;
            return CURRENT_METHOD_LOG + methodName;
        });
    }

    public static String getCurrentMethodLogTemplate() {
        return CURRENT_METHOD_LOG_TEMPLATE;
    }

    public static String getCurrentMethodString() {
        return CURRENT_METHOD_STRING_FORMAT;
    }

}
