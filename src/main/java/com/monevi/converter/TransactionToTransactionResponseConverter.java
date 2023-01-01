package com.monevi.converter;

import com.monevi.dto.response.TransactionResponse;
import com.monevi.entity.Transaction;
import org.springframework.stereotype.Component;

@Component(value = TransactionToTransactionResponseConverter.TRANSACTION_TO_TRANSACTION_RESPONSE_BEAN_NAME
    + Converter.SUFFIX_BEAN_NAME)
public class TransactionToTransactionResponseConverter implements Converter<Transaction, TransactionResponse> {

  public static final String TRANSACTION_TO_TRANSACTION_RESPONSE_BEAN_NAME = "TransactionToTransactionResponse";

  @Override
  public TransactionResponse convert(Transaction source) {
    return TransactionResponse.builder()
        .id(source.getId())
        .name(source.getName())
        .amount(source.getAmount())
        .entryPosition(source.getEntryPosition())
        .type(source.getType())
        .description(source.getDescription())
        .proof(source.getProof())
        .build();
  }
}
