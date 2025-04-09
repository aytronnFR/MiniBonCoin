package com.aytronn.demo1.config;

import com.aytronn.demo1.exception.ApiException;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<?> handleApiException(ApiException e) {
    return ResponseEntity
        .status(e.getStatus())
        .body(Map.of(
            "status", e.getStatus().value(),
            "message", e.getMessage(),
            "timestamp", System.currentTimeMillis()
        ));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleException(Exception e) {
    return ResponseEntity
        .status(500)
        .body(Map.of(
            "status", 500,
            "message", e.getMessage(),
            "timestamp", System.currentTimeMillis()
        ));
  }
}
