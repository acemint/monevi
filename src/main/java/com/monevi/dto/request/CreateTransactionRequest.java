package com.monevi.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monevi.constant.ErrorMessages;
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
public class CreateTransactionRequest {

  @NotBlank(message = ErrorMessages.MUST_NOT_BE_BLANK)
  private String organizationRegionId;

  @NotBlank(message = ErrorMessages.MUST_NOT_BE_BLANK)
  private String name;

  @ValidDate
  private String transactionDate;

  @Positive(message = ErrorMessages.MUST_BE_POSITIVE)
  private double amount;

  // TODO: create validation to make sure that only DEBIT/CREDIT can be inputted
  private EntryPosition entryPosition;

  // TODO: create validation to make sure that only enum of TransactionType can be inputted
  private TransactionType type;

  // TODO: create validation to make sure that only enum of GeneralLedgerAccountType can be inputted
  private GeneralLedgerAccountType generalLedgerAccountType;

  private String description;

  @NotNull(message = ErrorMessages.MUST_NOT_BE_BLANK)
  private String proof;
  
}
