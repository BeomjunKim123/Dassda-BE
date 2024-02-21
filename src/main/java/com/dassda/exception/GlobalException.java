package com.dassda.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleIllegalStateException(IllegalStateException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("errorCode", "500");
        response.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleUserNotFoundException(UsernameNotFoundException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("errorCode", "500");
        response.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("errorCode", "500");
        response.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("errorCode", "400");
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
