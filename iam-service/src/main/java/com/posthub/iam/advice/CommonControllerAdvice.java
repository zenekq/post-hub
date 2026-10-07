package com.posthub.iam.advice;

import com.posthub.iam.model.exception.DataExistException;
import com.posthub.iam.model.exception.InvalidDataException;
import com.posthub.iam.model.exception.InvalidPasswordException;
import com.posthub.iam.model.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.URI;
import java.net.http.HttpHeaders;
import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.List;

@Slf4j
@ControllerAdvice
public class CommonControllerAdvice {

    @ExceptionHandler
    @ResponseBody
    protected ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        log.error(ex.getMessage(), ex);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(DataExistException.class)
    @ResponseBody
    protected ResponseEntity<String> handleDataExistException(Exception ex) {
        log.error(ex.getMessage(), ex);

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
                                          HttpServletRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST.value());

        problemDetail.setTitle("VALIDATION_ERROR");
        problemDetail.setDetail("Validation failed");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        List<ValidationError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ValidationError(err.getField(), err.getDefaultMessage()))
                .toList();

        problemDetail.setProperty("errors", errors);
        problemDetail.setProperty("timestamp", Instant.now());

        log.warn("Validation failed: {}", errors);

        return problemDetail;
    }

    public record ValidationError(String field, String reason) {}

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    protected ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
    }

    @ExceptionHandler(InvalidDataException.class)
    @ResponseBody
    protected ResponseEntity<String> handleInvalidDataException(InvalidDataException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleInvalidPasswordException(InvalidPasswordException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    protected ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        log.error(ex.getMessage(), ex);

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ex.getMessage());
    }

}
