package com.calculator.exception;

import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHander {
  @ExceptionHandler(DateTimeParseException.class)
  public ResponseEntity<Map<String, List<String>>> handleDateValidationErrors(
      DateTimeParseException ex) {
    Map<String, List<String>> errorResponse = new HashMap<>();
    List<String> errors = Collections.singletonList(ex.getMessage());
    errorResponse.put("errors", errors);
    return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, List<String>>> handleInputValidationErrors(
          IllegalArgumentException ex) {
    Map<String, List<String>> errorResponse = new HashMap<>();
    List<String> errors = Collections.singletonList(ex.getMessage());
    errorResponse.put("errors", errors);
    return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }
}
