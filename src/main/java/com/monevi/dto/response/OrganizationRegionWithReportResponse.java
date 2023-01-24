package com.monevi.dto.response;

import com.monevi.enums.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationRegionWithReportResponse {

  private String reportId;
  private String organizationRegionId;
  private String organizationName;
  private String organizationAbbreviation;
  private Timestamp periodDate;
  private ReportStatus reportStatus;

}
