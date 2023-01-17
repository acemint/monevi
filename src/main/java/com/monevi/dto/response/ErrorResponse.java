package com.monevi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse implements Serializable {

  private String errorCode;

  private String errorMessage;

  @Builder.Default
  private Map<String, String> errorFields = new HashMap<>();

}
