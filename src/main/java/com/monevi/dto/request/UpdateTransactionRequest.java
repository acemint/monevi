package com.monevi.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
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
public class UpdateTransactionRequest {

  @NotBlank(message = ErrorMessages.MUST_NOT_BE_BLANK)
  private String name;

  @ValidDate
  private String transactionDate;

  @Positive(message = ErrorMessages.MUST_BE_POSITIVE)
  private double amount;

  private EntryPosition entryPosition;

  private TransactionType type;

  private GeneralLedgerAccountType generalLedgerAccountType;

  private String description;

  private String proof;
}
