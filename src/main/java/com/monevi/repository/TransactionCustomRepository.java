package com.monevi.repository;

import com.monevi.entity.Transaction;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetTransactionFilter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface TransactionCustomRepository {

  Page<Transaction> getTransactions(GetTransactionFilter filter)
      throws ApplicationException;

}
