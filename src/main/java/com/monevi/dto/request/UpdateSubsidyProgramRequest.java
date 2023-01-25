package com.monevi.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateSubsidyProgramRequest {

  @NotBlank
  private String name;

  @NotNull
  @Min(value = 0)
  private Double subsidy;

  @NotNull
  @Min(value = 1)
  private Double budget;
}
