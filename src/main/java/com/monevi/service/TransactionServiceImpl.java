package com.monevi.service;

import com.monevi.constant.ErrorMessages;
import com.monevi.dto.request.CreateTransactionRequest;
import com.monevi.entity.Report;
import com.monevi.entity.Transaction;
import com.monevi.enums.ReportStatus;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetReportFilter;
import com.monevi.repository.ReportRepository;
import com.monevi.repository.TransactionRepository;
import com.monevi.util.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private ReportRepository reportRepository;

  public Transaction createNewTransaction(CreateTransactionRequest request)
      throws ApplicationException {
    this.throwErrorOnExistingReportWithStatusApprovedBySupervisor(request.getOrganizationRegionId(), request.getTransactionDate());
    Transaction transaction = this.buildTransaction(request);
    return this.transactionRepository.save(transaction);
  }

  private void throwErrorOnExistingReportWithStatusApprovedBySupervisor(String organizationRegionId, String transactionDate)
      throws ApplicationException {
    List<Report> reports = this.reportRepository.getReports(GetReportFilter.builder()
            .organizationRegionId(organizationRegionId)
            .build())
        .orElse(Collections.emptyList());

    int newTransactionMonth = DateUtils.dateInputToMonth(transactionDate);
    int newTransactionYear = DateUtils.dateInputToYear(transactionDate);

    for (Report report : reports) {
      int existingReportMonth = new DateTime(report.getPeriodDate()).getMonthOfYear();
      int existingReportYear = new DateTime(report.getPeriodDate()).getYear();

      if (newTransactionMonth == existingReportMonth
          && newTransactionYear == existingReportYear
          && report.getStatus().equals(ReportStatus.APPROVED_BY_SUPERVISOR)) {
        throw new ApplicationException(HttpStatus.BAD_REQUEST,
            ErrorMessages.TRANSACTION_CANNOT_BE_CREATED_BECAUSE_REPORT_HAS_BEEN_APPROVED);
      }

    }
  }

  private Transaction buildTransaction(CreateTransactionRequest createTransactionRequest) throws ApplicationException {
    return Transaction.builder()
        .name(createTransactionRequest.getName())
        .transactionDate(DateUtils.dateInputToTimestamp(createTransactionRequest.getTransactionDate()))
        .amount(createTransactionRequest.getAmount())
        .entryPosition(createTransactionRequest.getEntryPosition())
        .type(createTransactionRequest.getType())
        .generalLedgerAccountType(createTransactionRequest.getGeneralLedgerAccountType())
        .description(createTransactionRequest.getDescription())
        .proof(createTransactionRequest.getProof().getBytes(StandardCharsets.UTF_8))
        .build();
  }

}
