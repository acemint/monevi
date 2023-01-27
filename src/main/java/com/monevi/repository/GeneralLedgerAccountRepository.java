package com.monevi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monevi.entity.Report;
import com.monevi.entity.ReportGeneralLedgerAccount;

public interface GeneralLedgerAccountRepository
    extends JpaRepository<ReportGeneralLedgerAccount, String> {

  Optional<List<ReportGeneralLedgerAccount>> findAllByReportAndMarkForDeleteFalse(Report report);
}
