package com.monevi.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monevi.enums.EntryPosition;
import com.monevi.enums.GeneralLedgerAccountType;
import com.monevi.enums.TransactionType;
import com.monevi.validation.annotation.ValidDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateTransactionRequest {

  @NotBlank
  private String name;

  @ValidDate
  private String transactionDate;

  @Positive
  private double amount;

  private EntryPosition entryPosition;

  private TransactionType type;

  private GeneralLedgerAccountType generalLedgerAccountType;

  @NotBlank
  private String description;

  private String proof;
}
