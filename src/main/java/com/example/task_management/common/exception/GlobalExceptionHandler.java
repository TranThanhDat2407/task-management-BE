package com.example.task_management.common.exception;

import com.example.task_management.common.exception.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    public ResponseEntity<ErrorResponse> handleAuthenticationErrors(RuntimeException ex, HttpServletRequest request) {
        log.info("Login failed: {}", ex.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .error("authentication error")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .status(HttpStatus.UNAUTHORIZED.value()) //401
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(InvalidTokenException.class)
    //handle invalid token
    public ResponseEntity<ErrorResponse> handleInvalidToken(InvalidTokenException ex, HttpServletRequest request) {
        log.warn("Auth error: {}", ex.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .error("unauthorized")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .status(HttpStatus.UNAUTHORIZED.value()) //401
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }


    //handle Unexpected System error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex, HttpServletRequest request) {
        log.error("Unexpected server error: {}", request.getRequestURI(), ex);

        ErrorResponse error = ErrorResponse.builder()
                .error("internal_error")
                .message("Đã có lỗi xảy ra, vui lòng thử lại sau")
                .path(request.getRequestURI())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value()) //500
                .build();

        return ResponseEntity.internalServerError().body(error);
    }
}
