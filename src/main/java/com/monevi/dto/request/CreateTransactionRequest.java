package com.monevi.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monevi.enums.EntryPosition;
import com.monevi.enums.GeneralLedgerAccountType;
import com.monevi.enums.TransactionType;
import com.monevi.validation.annotation.ValidDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateTransactionRequest {

  @NotBlank
  private String organizationRegionId;

  @NotBlank
  private String name;

  @ValidDate
  private String transactionDate;

  @Positive
  private double amount;

  // TODO: create validation to make sure that only DEBIT/CREDIT can be inputted
  private EntryPosition entryPosition;

  // TODO: create validation to make sure that only enum of TransactionType can be inputted
  private TransactionType type;

  // TODO: create validation to make sure that only enum of GeneralLedgerAccountType can be inputted
  private GeneralLedgerAccountType generalLedgerAccountType;

  @NotBlank
  private String description;

  @NotNull
  private String proof;
  
}
