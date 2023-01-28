package com.monevi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monevi.entity.OrganizationRegion;
import com.monevi.entity.Wallet;
import com.monevi.enums.GeneralLedgerAccountType;

public interface WalletRepository extends JpaRepository<Wallet, String> {

  Optional<Wallet> findByOrganizationRegionAndNameAndMarkForDeleteFalse(
      OrganizationRegion organizationRegion, GeneralLedgerAccountType type);
}
