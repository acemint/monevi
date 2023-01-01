package com.monevi.repository;

import com.monevi.entity.Transaction;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetTransactionFilter;

import java.util.List;
import java.util.Optional;

public interface TransactionCustomRepository {

  Optional<List<Transaction>> getTransactions(GetTransactionFilter filter)
      throws ApplicationException;

}
