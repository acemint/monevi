package com.monevi.dto.vo;

import java.sql.Timestamp;

import com.monevi.enums.ReportStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportHistoryFindResponseVO {

  private String organizationName;
  private Timestamp reportPeriod;
  private ReportStatus remarks;
  private String userName;
  private Timestamp createdDate;
}
