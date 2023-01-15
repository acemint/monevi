package com.monevi.repository;

import com.monevi.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository
    extends JpaRepository<Transaction, String>, TransactionCustomRepository {

  Optional<Transaction> findByIdAndMarkForDeleteFalse(String transactionId);
  String QUERY_GROUPING_TRANSACTION_FOR_REPORT_DISPLAY =
      "SELECT SUM(amount) AS amount, entry_position, general_ledger_account_type, \"type\" "
          + "FROM monevi_transaction mt "
          + "WHERE report_id = :reportId "
          + "GROUP BY entry_position, general_ledger_account_type, \"type\"";

  @Query(value = QUERY_GROUPING_TRANSACTION_FOR_REPORT_DISPLAY, nativeQuery = true)
  List<Tuple> groupingTransactionForReportDisplay(@Param("reportId") String reportId);

}
