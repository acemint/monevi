package com.monevi.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
public class ApplicationException extends Exception {

  private HttpStatus httpStatus;
  private Map<String, String> errorFields;

  public ApplicationException(HttpStatus httpStatus, Map<String, String> errorFields) {
    this.httpStatus = httpStatus;
    this.errorFields = Optional.ofNullable(errorFields).orElse(new HashMap<>());
  }


}
