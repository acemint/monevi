package com.monevi.repository;

import org.springframework.data.domain.Page;

import com.monevi.entity.Transaction;
import com.monevi.enums.GeneralLedgerAccountType;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetTransactionFilter;

public interface TransactionCustomRepository {

  Page<Transaction> getTransactions(GetTransactionFilter filter)
      throws ApplicationException;

  Double calculateTotalByGeneralLedgerAccountTypeAndOrganizationRegionId(
      GeneralLedgerAccountType type, String organizationRegionId) throws ApplicationException;
}
