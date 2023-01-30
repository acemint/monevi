package com.monevi.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monevi.constant.ErrorMessages;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportApproveRequest {

  @NotBlank(message = ErrorMessages.MUST_NOT_BE_BLANK)
  private String reportId;

  @NotBlank(message = ErrorMessages.MUST_NOT_BE_BLANK)
  private String userId;

}
