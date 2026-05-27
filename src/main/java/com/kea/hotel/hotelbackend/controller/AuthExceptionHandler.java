package com.kea.hotel.hotelbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice(assignableTypes = AuthController.class)
public class AuthExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        return buildResponse(
                HttpStatus.UNAUTHORIZED,
                "Invalid username or password.",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(AuthenticationServiceException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationService(AuthenticationServiceException ex, HttpServletRequest request) {
        return buildResponse(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Authentication service is temporarily unavailable. Please try again shortly.",
                request.getRequestURI()
        );
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message, String path) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", path);
        return ResponseEntity.status(status).body(body);
    }
}