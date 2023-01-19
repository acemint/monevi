package com.monevi.model;

import com.monevi.enums.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetReportFilter {

  private String regionId;
  private String organizationRegionId;
  private String startDate;
  private String endDate;
  private ReportStatus reportStatus;
  private Pageable pageable;

}
