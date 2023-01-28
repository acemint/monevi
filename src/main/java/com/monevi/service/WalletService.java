package com.monevi.service;

import com.monevi.entity.Transaction;
import com.monevi.entity.Wallet;
import com.monevi.enums.GeneralLedgerAccountType;
import com.monevi.exception.ApplicationException;

public interface WalletService {

  Wallet findWalletByGeneralLedgerAccountTypeAndOrganizationRegion(GeneralLedgerAccountType type,
      String organizationRegionId) throws ApplicationException;

  void recalculateWalletByGeneralLedgerAccountType(Transaction transaction)
      throws ApplicationException;

  void recalculateWalletByRemovedTransaction(Transaction transaction) throws ApplicationException;
}
