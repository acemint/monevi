package com.monevi.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monevi.constant.ErrorMessages;
import com.monevi.enums.GeneralLedgerAccountType;
import com.monevi.validation.annotation.ValidDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubmitReportRequest {

  @NotBlank(message = ErrorMessages.MUST_NOT_BE_BLANK)
  private String organizationRegionId;

  @ValidDate
  private String date;

  @NotBlank(message = ErrorMessages.MUST_NOT_BE_BLANK)
  private String userId;

  private Map<GeneralLedgerAccountType, Double> opnameData;

}
