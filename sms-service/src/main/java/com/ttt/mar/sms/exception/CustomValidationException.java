package com.ttt.mar.sms.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author linhdev
 * @date 20/02/2020
 */

@ControllerAdvice
public class CustomValidationException extends ResponseEntityExceptionHandler {


  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    Map<String, Object> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put("message", errorMessage);
      errors.put("timestamp", new Date(System.currentTimeMillis()));
      errors.put("status", false);
    });
    return new ResponseEntity<>(errors, headers, HttpStatus.OK);
  }
}
