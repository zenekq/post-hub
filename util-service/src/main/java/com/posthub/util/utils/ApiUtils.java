package com.posthub.util.utils;

import com.posthub.util.model.constant.ApiConstants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiUtils {

    private static final StackWalker WALKER =
            StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    public static String getMethodName() {
        try {
            return WALKER.walk(frames ->
                    frames.skip(1).findFirst().map(StackWalker.StackFrame::getMethodName)
                            .orElse(ApiConstants.UNDEFINED)
            );
        } catch (Exception e) {
            return ApiConstants.UNDEFINED;
        }
    }


}
