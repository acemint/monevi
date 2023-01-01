package com.monevi.service;

import com.monevi.dto.request.CreateNewTransactionRequest;
import com.monevi.entity.Transaction;
import com.monevi.exception.ApplicationException;

public interface TransactionService {

  Transaction createNewTransaction(CreateNewTransactionRequest request)
      throws ApplicationException;
}
