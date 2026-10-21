package com.posthub.util.advice;

import com.posthub.util.model.constant.ApiErrorMessage;
import com.posthub.util.model.exception.AuthException;
import com.posthub.util.model.exception.BadRequestException;
import com.posthub.util.model.exception.InternalException;
import com.posthub.util.model.exception.NotFoundException;
import com.posthub.util.model.response.UtilsResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.time.zone.ZoneRulesException;

@Slf4j
@ControllerAdvice(annotations = RestController.class)
@RequiredArgsConstructor
public class CommonControllerAdvice {

    @ExceptionHandler
    @ResponseBody
    protected ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        log.error(ex.getMessage(), ex);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<UtilsResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(UtilsResponse.createFailed(ex.getMessage()));
    }

    @ExceptionHandler(ZoneRulesException.class)
    @ResponseBody
    protected ResponseEntity<UtilsResponse<String>> handleNotFoundException(ZoneRulesException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new UtilsResponse<>(ex.getMessage(), null, false));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseBody
    protected ResponseEntity<UtilsResponse<String>> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new UtilsResponse<>(ex.getMessage(), null, false));
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    protected ResponseEntity<UtilsResponse<String>> handleBadRequestException(BadRequestException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new UtilsResponse<>(ex.getMessage(), null, false));
    }

    @ExceptionHandler(AuthException.class)
    @ResponseBody
    protected ResponseEntity<UtilsResponse<String>> handleAuthException(AuthException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new UtilsResponse<>(ex.getMessage(), null, false));
    }

    @ExceptionHandler(InternalException.class)
    @ResponseBody
    protected ResponseEntity<String> handleUndefinedException(InternalException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiErrorMessage.UNEXPECTED_ERROR.getMessage());
    }
}
