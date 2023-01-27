package com.monevi.service.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.monevi.constant.ErrorMessages;
import com.monevi.entity.OrganizationRegion;
import com.monevi.entity.Transaction;
import com.monevi.entity.Wallet;
import com.monevi.enums.EntryPosition;
import com.monevi.enums.GeneralLedgerAccountType;
import com.monevi.exception.ApplicationException;
import com.monevi.repository.OrganizationRegionRepository;
import com.monevi.repository.TransactionRepository;
import com.monevi.repository.WalletRepository;
import com.monevi.service.WalletService;

@Service
public class WalletServiceImpl implements WalletService {

  @Autowired
  private WalletRepository walletRepository;

  @Autowired
  private OrganizationRegionRepository organizationRegionRepository;

  @Autowired
  private TransactionRepository transactionRepository;

  @Override
  public Wallet findWalletByGeneralLedgerAccountTypeAndOrganizationRegion(
      GeneralLedgerAccountType type, String organizationRegionId) throws ApplicationException {
    OrganizationRegion organizationRegion =
        this.organizationRegionRepository.findByIdAndMarkForDeleteIsFalse(organizationRegionId)
            .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorMessages.ORGANIZATION_REGION_DOES_NOT_EXISTS));
    Wallet existingWallet = this.walletRepository
        .findByOrganizationRegionAndNameAndMarkForDeleteFalse(organizationRegion, type)
        .orElse(null);

    if (Objects.isNull(existingWallet)) {
      return this.createNewWallet(type, organizationRegion);
    }
    return existingWallet;
  }

  private Wallet createNewWallet(GeneralLedgerAccountType type,
      OrganizationRegion organizationRegion) throws ApplicationException {
    Double total =
        this.transactionRepository.calculateTotalByGeneralLedgerAccountTypeAndOrganizationRegionId(
            type, organizationRegion.getId());
    Wallet cashWallet = Wallet.builder()
        .name(type)
        .total(total)
        .organizationRegion(organizationRegion)
        .build();
    return this.walletRepository.save(cashWallet);
  }

  @Override
  public void recalculateWalletByGeneralLedgerAccountType(Transaction transaction)
      throws ApplicationException {
    Wallet existingWallet =
        this.walletRepository
            .findByOrganizationRegionAndNameAndMarkForDeleteFalse(
                transaction.getOrganizationRegion(), transaction.getGeneralLedgerAccountType())
            .orElse(null);

    if (Objects.isNull(existingWallet)) {
      this.createNewWallet(transaction.getGeneralLedgerAccountType(),
          transaction.getOrganizationRegion());
      return;
    }
    existingWallet.setTotal(this.calculateByEntryPosition(existingWallet.getTotal(), transaction));
    this.walletRepository.save(existingWallet);
  }

  private Double calculateByEntryPosition(Double existingTotal, Transaction transaction) {
    if (EntryPosition.DEBIT.equals(transaction.getEntryPosition())) {
      return existingTotal + transaction.getAmount();
    } else if (EntryPosition.CREDIT.equals(transaction.getEntryPosition())) {
      return existingTotal - transaction.getAmount();
    }
    return 0d;
  }

  @Override
  public void recalculateWalletByRemovedTransaction(Transaction transaction)
      throws ApplicationException {
    Wallet existingWallet =
        this.walletRepository
            .findByOrganizationRegionAndNameAndMarkForDeleteFalse(
                transaction.getOrganizationRegion(), transaction.getGeneralLedgerAccountType())
            .orElse(null);

    if (Objects.isNull(existingWallet)) {
      this.createNewWallet(transaction.getGeneralLedgerAccountType(),
          transaction.getOrganizationRegion());
      return;
    }
    existingWallet
        .setTotal(this.recalculateRemovedTransaction(existingWallet.getTotal(), transaction));
    this.walletRepository.save(existingWallet);
  }

  private Double recalculateRemovedTransaction(Double existingTotal, Transaction transaction) {
    if (EntryPosition.DEBIT.equals(transaction.getEntryPosition())) {
      return existingTotal - transaction.getAmount();
    } else if (EntryPosition.CREDIT.equals(transaction.getEntryPosition())) {
      return existingTotal + transaction.getAmount();
    }
    return 0d;
  }
}
