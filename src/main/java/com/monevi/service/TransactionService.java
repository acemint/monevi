package com.monevi.service;

import com.monevi.dto.request.CreateTransactionRequest;
import com.monevi.dto.request.UpdateTransactionRequest;
import com.monevi.entity.Transaction;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetTransactionFilter;

import java.util.List;

public interface TransactionService {

  Transaction createNewTransaction(CreateTransactionRequest request)
      throws ApplicationException;

  List<Transaction> getTransactions(GetTransactionFilter filter)
      throws ApplicationException;

  Transaction updateTransaction(String transactionId, UpdateTransactionRequest request)
      throws ApplicationException;
}
