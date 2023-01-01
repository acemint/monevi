package com.monevi.service;

import com.monevi.dto.request.CreateTransactionRequest;
import com.monevi.entity.Transaction;
import com.monevi.exception.ApplicationException;

public interface TransactionService {

  Transaction createNewTransaction(CreateTransactionRequest request)
      throws ApplicationException;
}
