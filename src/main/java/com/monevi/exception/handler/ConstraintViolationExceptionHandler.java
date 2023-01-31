package com.monevi.exception.handler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.monevi.dto.response.ErrorResponse;

@ControllerAdvice
public class ConstraintViolationExceptionHandler {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(
      ConstraintViolationException ex) {
    ErrorResponse errorResponse =
        ErrorResponse.builder().errorFields(convertToMap(ex.getConstraintViolations())).build();
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  private Map<String, String> convertToMap(Set<ConstraintViolation<?>> errors) {
    if (!errors.isEmpty()) {
      Map<String, String> map = new HashMap<>();
      for (ConstraintViolation<?> data : errors) {
        String variable = data.getPropertyPath().toString();
        String error = map.get(variable);
        if (StringUtils.isNotBlank(error)) {
          error = error + ", " + data.getMessage();
          map.put(variable, error);
        } else {
          String newError = data.getMessage();
          map.put(variable, newError);
        }
      }
      return map;
    } else {
      return Collections.emptyMap();
    }
  }
}
