package com.monevi.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monevi.constant.ErrorMessages;
import com.monevi.validation.annotation.ValidDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProgramRequest {

  @NotBlank(message = ErrorMessages.MUST_NOT_BE_BLANK)
  private String organizationRegionId;

  @NotBlank(message = ErrorMessages.MUST_NOT_BE_BLANK)
  private String programName;

  @Positive(message = ErrorMessages.MUST_BE_POSITIVE)
  private double budget;

  @PositiveOrZero(message = ErrorMessages.MUST_BE_POSITIVE_OR_ZERO)
  private double subsidy;

  @ValidDate
  private String startDate;

  @ValidDate
  private String endDate;

  @NotBlank(message = ErrorMessages.MUST_NOT_BE_BLANK)
  private String userId;

}
