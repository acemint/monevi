package com.monevi.controller;

import com.monevi.dto.response.BaseResponse;
import com.monevi.exception.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<BaseResponse<?>> handleApplicationException(ApplicationException ex) {
      Map<String, String> errors = new HashMap<>();
      errors.put("errorCode", String.valueOf(ex.getHttpStatus().value()));
      errors.put("errorType", ex.getHttpStatus().name());
      errors.put("name", ex.getErrorMessage());
      BaseResponse<String> baseResponse = BaseResponse.<String>builder()
          .value("HEHE")
          .errors(errors)
          .build();
      if (ex.getHttpStatus().value() == 400) {
        return new ResponseEntity<>(baseResponse, ex.getHttpStatus());
      }
      else if (ex.getHttpStatus().value() == 500) {
        return new ResponseEntity<>(baseResponse, ex.getHttpStatus());
      }
      return new ResponseEntity<>(baseResponse, ex.getHttpStatus());
    }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<BaseResponse<?>> forwardHandleBadRequest(ResponseEntity<BaseResponse<?>> response) {
    return response;
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<BaseResponse<?>> forwardHandleInternalServerError(ResponseEntity<BaseResponse<?>> response) {
    return response;
  }

}