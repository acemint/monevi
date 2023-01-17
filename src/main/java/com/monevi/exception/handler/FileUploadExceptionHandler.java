package com.monevi.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.monevi.constant.ErrorMessages;
import com.monevi.dto.response.ErrorResponse;

@ControllerAdvice
public class FileUploadExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<ErrorResponse> handleMaxSizeException(MaxUploadSizeExceededException ex) {
    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .errorCode(ex.getMessage())
            .errorMessage(ErrorMessages.FILE_TOO_LARGE).build();
    return new ResponseEntity<>(errorResponse, HttpStatus.EXPECTATION_FAILED);
  }
}
