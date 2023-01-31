package com.monevi.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.monevi.constant.ErrorMessages;
import com.monevi.validation.annotation.ValidDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateProgramRequest {

  @NotBlank(message = ErrorMessages.MUST_NOT_BE_BLANK)
  private String name;

  @NotNull(message = ErrorMessages.MUST_NOT_BE_BLANK)
  @Min(value = 0)
  private Double subsidy;

  @NotNull(message = ErrorMessages.MUST_NOT_BE_BLANK)
  @Min(value = 1)
  private Double budget;

  @ValidDate
  private String startDate;

  @ValidDate
  private String endDate;
}
