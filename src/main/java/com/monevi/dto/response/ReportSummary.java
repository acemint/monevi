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

  private String reportId;

  @Builder.Default
  private Map<GeneralLedgerAccountType, GeneralLedgerData> generalLedgerAccountTypeData = new HashMap<>();

  @Data
  @Builder
  public static class GeneralLedgerData {

    @Builder.Default
    private Map<TransactionType, TransactionTypeData> transactionTypeData = new HashMap<>();
    private double previousMonthBalance;
    private double opnameAmount;

  }

  @Data
  @Builder
  public static class TransactionTypeData {

    @Builder.Default
    private Map<EntryPosition, EntryPositionData> entryPositionData = new HashMap<>();


  }

  @Data
  @Builder
  public static class EntryPositionData {

    private double amount;

  }

}
