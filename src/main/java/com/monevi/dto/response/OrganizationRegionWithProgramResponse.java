package com.monevi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationRegionWithProgramResponse {

  private String organizationRegionId;
  private String organizationName;
  private String organizationAbbreviation;
  private Integer periodYear;

}
