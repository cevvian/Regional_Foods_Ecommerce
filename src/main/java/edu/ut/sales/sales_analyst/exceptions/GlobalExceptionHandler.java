package edu.ut.sales.sales_analyst.exceptions;

import edu.ut.sales.sales_analyst.model.dtos.responses.ResponseAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseAPI<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return new ResponseEntity<>(
                new ResponseAPI<>("Validation failed", HttpStatus.BAD_REQUEST, errorMessages),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ResponseAPI<Object>> handleAppException(AppException ex) {
        return new ResponseEntity<>(
                new ResponseAPI<>(ex.getMessage(), (HttpStatus) ex.getErrorCode().getStatusCode(), null),
                ex.getErrorCode().getStatusCode()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseAPI<Object>> handleOtherExceptions(Exception ex) {
        logger.error("Unexpected error occurred", ex);
        return new ResponseEntity<>(
                new ResponseAPI<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseAPI<Void> handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseAPI<>("Wrong email or password", HttpStatus.UNAUTHORIZED, null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseAPI<Void> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseAPI<>("Access Denied", HttpStatus.FORBIDDEN);
    }
}

