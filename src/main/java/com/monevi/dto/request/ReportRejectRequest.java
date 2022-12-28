package com.monevi.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class ReportRejectRequest {

  @NotBlank
  private String reportId;

  @NotBlank
  private String userId;

  @NotBlank
  private String comment;

}
