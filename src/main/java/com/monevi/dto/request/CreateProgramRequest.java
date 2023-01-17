package com.monevi.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

  @NotBlank
  private String organizationRegionId;

  @NotBlank
  private String programName;

  @Positive
  private double budget;

  @PositiveOrZero
  private double subsidy;

  @ValidDate
  private String startDate;

  @ValidDate
  private String endDate;

}
