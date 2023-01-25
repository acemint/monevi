package com.monevi.model;

import com.monevi.enums.UserAccountRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetReportHistoryFilter {

  private String regionId;
  private String organizationRegionId;
  private Integer termOfOffice;
  private UserAccountRole userRole;
  private String userId;
  private Pageable pageable;
}
