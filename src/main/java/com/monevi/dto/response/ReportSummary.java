package com.monevi.dto.response;

import com.monevi.enums.EntryPosition;
import com.monevi.enums.GeneralLedgerAccountType;
import com.monevi.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportSummary {

  @Builder.Default
  private Map<GeneralLedgerAccountType, GeneralLedgerReport> generalLedgerAccountMap = new HashMap<>();



  @Data
  @Builder
  public static class GeneralLedgerReport {

    @Builder.Default
    private Map<TransactionType, TransactionTypeReport> transactionTypeMap = new HashMap<>();
    private double previousMonthBalance;
    private double opnameAmount;

  }

  @Data
  @Builder
  public static class TransactionTypeReport {

    private EntryPosition entryPosition;
    private double amount;

  }

}
