package com.monevi.dto.response;

import com.monevi.enums.EntryPosition;
import com.monevi.enums.GeneralLedgerAccountType;
import com.monevi.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse implements Serializable {

  private String id;
  private String name;
  private long transactionDate;
  private double amount;
  private EntryPosition entryPosition;
  private TransactionType type;
  private GeneralLedgerAccountType generalLedgerAccountType;
  private String description;
  private byte[] proof;

}
