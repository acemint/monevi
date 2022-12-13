package com.monevi.controller;

import com.monevi.dto.response.ErrorResponse;
import com.monevi.exception.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException ex) {
      ErrorResponse errorResponse = ErrorResponse.builder()
          .errorFields(ex.getErrorFields())
          .build();
      if (ex.getHttpStatus().value() == 400) {
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
      }
      else if (ex.getHttpStatus().value() == 500) {
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
      }
      return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> forwardHandleBadRequest(ResponseEntity<ErrorResponse> response) {
    return response;
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ErrorResponse> forwardHandleInternalServerError(ResponseEntity<ErrorResponse> response) {
    return response;
  }

}