package com.monevi.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

@Getter
@Setter
public class ApplicationException extends Exception {

  private HttpStatus httpStatus;
  private String errorMessage;

  public ApplicationException(HttpStatus httpStatus, @Nullable String errorMessage) {
    this.httpStatus = httpStatus;
    this.errorMessage = errorMessage;
  }

}
