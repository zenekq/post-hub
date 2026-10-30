package com.gateway.advice;

import com.gateway.model.constants.ApiConstants;
import com.gateway.model.exception.AccessException;
import com.gateway.model.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Objects;

@Slf4j
@ControllerAdvice(annotations = RestController.class)
public class CommonControllerAdvice {

    @ExceptionHandler(AccessException.class)
    private ResponseEntity<String> handleAccessException(AccessException ex) {
        logStackTrace(ex);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    private ResponseEntity<String> handleBadRequestException(BadRequestException ex) {
        logStackTrace(ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    protected ResponseEntity<String> handleUndefinedException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }

    private void logStackTrace(Exception ex) {
        StringBuilder stackTrace = new StringBuilder();
        stackTrace.append(ex.getMessage() ).append(ApiConstants.BREAK_LINE);
        if (Objects.nonNull(ex.getCause())) {
            stackTrace.append(ex.getCause().getMessage()).append(ApiConstants.BREAK_LINE);
        }

        String packageName;
        String exPackageName = ex.getClass().getPackageName();
        if (exPackageName.contains(ApiConstants.DEFAULT_PACKAGE_NAME)) {
            packageName = ApiConstants.DEFAULT_PACKAGE_NAME;
        } else {
            packageName = StringUtils.EMPTY;
        }

        Arrays.stream(ex.getStackTrace())
                .filter(st -> Objects.nonNull(st) && st.getLineNumber() > 0 && st.getClassName().contains(packageName))
                .forEach(st -> stackTrace
                        .append(ApiConstants.DEFAULT_WHITESPACES_BEFORE_STACK_TRACE)
                        .append(ApiConstants.ANSI_RED)
                        .append(st.getClassName())
                        .append(".")
                        .append(st.getMethodName())
                        .append(" (")
                        .append(st.getLineNumber())
                        .append(") ")
                        .append(ApiConstants.BREAK_LINE)
                );
        log.error(stackTrace.append(ApiConstants.ANSI_WHITE).toString());
    }

}
