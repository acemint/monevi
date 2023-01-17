package com.monevi.converter;

import com.monevi.dto.response.TransactionResponse;
import com.monevi.entity.Program;
import com.monevi.entity.Transaction;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component(value = TransactionToTransactionResponseConverter.TRANSACTION_TO_TRANSACTION_RESPONSE_BEAN_NAME
    + Converter.SUFFIX_BEAN_NAME)
public class TransactionToTransactionResponseConverter implements Converter<Transaction, TransactionResponse> {

  public static final String TRANSACTION_TO_TRANSACTION_RESPONSE_BEAN_NAME = "TransactionToTransactionResponse";

  @Override
  public TransactionResponse convert(Transaction source) {
    TransactionResponse response = TransactionResponse.builder()
        .id(source.getId())
        .name(source.getName())
        .amount(source.getAmount())
        .entryPosition(source.getEntryPosition())
        .generalLedgerAccountType(source.getGeneralLedgerAccountType())
        .type(source.getType())
        .transactionDate(source.getTransactionDate().getTime())
        .description(source.getDescription())
        .proof(source.getProof())
        .build();
    if(Objects.nonNull(source.getProgram())) {
      response.setProgram(source.getProgram().getName());
    }
    return response;
  }
}
