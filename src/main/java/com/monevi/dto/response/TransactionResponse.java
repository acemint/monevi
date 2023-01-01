package com.monevi.dto.response;

import com.monevi.enums.EntryPosition;
import com.monevi.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse implements Serializable {

  private String id;
  private String name;
  private DateTime transactionDate;
  private double amount;
  private EntryPosition entryPosition;
  private TransactionType type;
  private String description;
  private byte[] proof;

}
