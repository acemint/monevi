package com.monevi.dto.response;

import com.monevi.enums.GeneralLedgerAccountType;
import com.monevi.enums.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {

  private String id;
  private long periodDate;
  private ReportStatus status;
  private String commentedBy;
  private String comment;
  private List<ReportGeneralLedgerAccountResponse> generalLedgerAccountValues;

  @Data
  @Builder
  public static class ReportGeneralLedgerAccountResponse {

    private GeneralLedgerAccountType name;
    private double amount;


  }

}
