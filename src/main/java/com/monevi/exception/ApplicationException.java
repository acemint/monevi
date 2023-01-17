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

  private static final String DEFAULT_KEY = "key";

  private HttpStatus httpStatus;
  private Map<String, String> errorFields;

  public ApplicationException(HttpStatus httpStatus, Map<String, String> errorFields) {
    this.httpStatus = httpStatus;
    this.errorFields = Optional.ofNullable(errorFields).orElse(new HashMap<>());
  }

  public ApplicationException(HttpStatus httpStatus, String errorField, String errorMessage) {
    Map<String, String> hashMap = new HashMap<>();
    hashMap.put(errorField, errorMessage);
    this.httpStatus = httpStatus;
    this.errorFields = hashMap;
  }

  public ApplicationException(HttpStatus httpStatus, String errorMessage) {
    Map<String, String> hashMap = new HashMap<>();
    hashMap.put(DEFAULT_KEY, errorMessage);
    this.httpStatus = httpStatus;
    this.errorFields = hashMap;
  }


}
