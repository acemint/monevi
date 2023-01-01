package com.monevi.controller;

import com.monevi.converter.Converter;
import com.monevi.converter.TransactionToTransactionResponseConverter;
import com.monevi.dto.request.CreateNewTransactionRequest;
import com.monevi.dto.response.BaseResponse;
import com.monevi.dto.response.TransactionResponse;
import com.monevi.entity.Transaction;
import com.monevi.exception.ApplicationException;
import com.monevi.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(ApiPath.BASE + ApiPath.TRANSACTION)
public class TransactionController {

  @Autowired
  private TransactionService transactionService;

  @Autowired
  @Qualifier(TransactionToTransactionResponseConverter.TRANSACTION_TO_TRANSACTION_RESPONSE_BEAN_NAME + Converter.SUFFIX_BEAN_NAME)
  private Converter<Transaction, TransactionResponse> transactionToTransactionResponseConverter;

  @PostMapping(value = ApiPath.CREATE_NEW, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<TransactionResponse> createNewTransaction(
      @Valid @RequestBody CreateNewTransactionRequest createNewTransactionRequest) throws ApplicationException {
    Transaction newTransaction = this.transactionService.createNewTransaction(createNewTransactionRequest);
    return BaseResponse.<TransactionResponse>builder()
        .value(this.transactionToTransactionResponseConverter.convert(newTransaction))
        .build();
  }


}
