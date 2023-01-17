package com.monevi.model;

import com.monevi.enums.EntryPosition;
import com.monevi.enums.GeneralLedgerAccountType;
import com.monevi.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetTransactionFilter {

  private String organizationRegionId;
  private String regionId;
  private String startDate;
  private String endDate;
  private Pageable pageable;
  private TransactionType transactionType;
  private GeneralLedgerAccountType generalLedgerAccountType;
  private EntryPosition entryPosition;
}
