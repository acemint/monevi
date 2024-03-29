package com.monevi.service.impl;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.monevi.entity.ReportComment;
import com.monevi.entity.ReportGeneralLedgerAccount;
import com.monevi.repository.GeneralLedgerAccountRepository;
import com.monevi.repository.ReportCommentRepository;
import com.monevi.service.WalletService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.monevi.constant.ErrorMessages;
import com.monevi.dto.request.CreateTransactionRequest;
import com.monevi.dto.request.UpdateTransactionRequest;
import com.monevi.entity.OrganizationRegion;
import com.monevi.entity.Report;
import com.monevi.entity.Transaction;
import com.monevi.enums.ReportStatus;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetReportFilter;
import com.monevi.model.GetTransactionFilter;
import com.monevi.repository.OrganizationRegionRepository;
import com.monevi.repository.ReportRepository;
import com.monevi.repository.TransactionRepository;
import com.monevi.service.TransactionService;
import com.monevi.util.DateUtils;

@Service
public class TransactionServiceImpl implements TransactionService {

  @Autowired
  private OrganizationRegionRepository organizationRegionRepository;

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private ReportRepository reportRepository;
  
  @Autowired
  private GeneralLedgerAccountRepository generalLedgerAccountRepository;
  
  @Autowired
  private ReportCommentRepository reportCommentRepository;

  @Autowired
  private WalletService walletService;

  @Override
  public List<Transaction> createTransactions(List<CreateTransactionRequest> requests)
      throws ApplicationException {
    return Optional.ofNullable(requests).map(requestList -> requestList.stream().map(request -> {
      try {
        return this.createNewTransaction(request);
      } catch (ApplicationException e) {
        throw new RuntimeException(e);
      }
    }).collect(Collectors.toList()))
        .orElseThrow(() -> new ApplicationException(HttpStatus.BAD_REQUEST,
            ErrorMessages.FAIL_TO_CREATE_TRANSACTION));
  }

  private Transaction createNewTransaction(CreateTransactionRequest request)
      throws ApplicationException {
    this.throwErrorOnExistingReportWithStatusApprovedBySupervisor(request.getOrganizationRegionId(),
        request.getTransactionDate());
    OrganizationRegion organizationRegion = this.organizationRegionRepository
        .findByIdAndMarkForDeleteIsFalse(request.getOrganizationRegionId())
        .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorMessages.ORGANIZATION_REGION_DOES_NOT_EXISTS));
    this.checkMonthlyReport(organizationRegion, request.getTransactionDate());
    this.validateTransactionDate(request.getTransactionDate());
    Transaction transaction = this.buildTransaction(request);
    transaction.setOrganizationRegion(organizationRegion);
    this.transactionRepository.save(transaction);
    this.walletService.recalculateWalletByGeneralLedgerAccountType(transaction);
    return transaction;
  }

  private void validateTransactionDate(String transactionDate) throws ApplicationException {
    Long date = DateUtils.convertDateToLong(transactionDate);
    if (date > System.currentTimeMillis()) {
      throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.INVALID_TRANSACTION_DATE);
    }
  }

  private void checkMonthlyReport(OrganizationRegion organizationRegion, String date)
      throws ApplicationException {
    Timestamp periodDate = DateUtils.dateInputToTimestamp(DateUtils.dateToFirstDayOfMonth(date));
    Report currentMonthReport = this.reportRepository
        .findByOrganizationRegionAndPeriodDateAndMarkForDeleteFalse(organizationRegion, periodDate)
        .orElse(null);
    if (Objects.nonNull(currentMonthReport)) {
      if (!isReportStatusValid(currentMonthReport.getStatus())) {
        throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorMessages.REPORT_HANDLING_IS_PROHIBITED);
      } else {
        currentMonthReport.setMarkForDelete(true);
        this.reportRepository.save(currentMonthReport);

        List<ReportGeneralLedgerAccount> generalLedgerAccounts = this.generalLedgerAccountRepository
            .findAllByReportAndMarkForDeleteFalse(currentMonthReport).orElse(null);
        if (Objects.nonNull(generalLedgerAccounts)) {
          generalLedgerAccounts = generalLedgerAccounts.stream().map(generalLedgerAccount -> {
            generalLedgerAccount.setMarkForDelete(true);
            return generalLedgerAccount;
          }).collect(Collectors.toList());
          this.generalLedgerAccountRepository.saveAll(generalLedgerAccounts);
        }

        ReportComment comment = this.reportCommentRepository
            .findByReportAndMarkForDeleteFalse(currentMonthReport).orElse(null);
        if (Objects.nonNull(comment)) {
          comment.setMarkForDelete(true);
          this.reportCommentRepository.save(comment);
        }
      }
    }
  }

  private boolean isReportStatusValid(ReportStatus reportStatus) {
    return ReportStatus.NOT_SENT.equals(reportStatus) || ReportStatus.DECLINED.equals(reportStatus);
  }

  @Override
  public Page<Transaction> getTransactions(GetTransactionFilter filter)
      throws ApplicationException {
    return this.transactionRepository.getTransactions(filter);
  }

  private void throwErrorOnExistingReportWithStatusApprovedBySupervisor(String organizationRegionId, String transactionDate)
      throws ApplicationException {
    List<Report> reports = this.reportRepository
        .getReports(GetReportFilter.builder().organizationRegionId(organizationRegionId)
            .pageable(PageRequest.of(0, Integer.MAX_VALUE)).build())
        .getContent();

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

  @Override
  public Transaction updateTransaction(String transactionId, UpdateTransactionRequest request)
      throws ApplicationException {
    Transaction existingTransaction =
        this.transactionRepository.findByIdAndMarkForDeleteFalse(transactionId)
            .orElseThrow(() -> new ApplicationException(HttpStatus.BAD_REQUEST,
                ErrorMessages.TRANSACTION_NOT_FOUND));
    this.throwErrorOnExistingReportWithStatusApprovedBySupervisor(
        existingTransaction.getOrganizationRegion().getId(),
        DateUtils.convertTimestampToString(existingTransaction.getTransactionDate()));
    this.checkMonthlyReport(existingTransaction.getOrganizationRegion(),
        DateUtils.convertTimestampToString(existingTransaction.getTransactionDate()));
    this.walletService.recalculateWalletByRemovedTransaction(existingTransaction);

    this.validateTransactionDate(request.getTransactionDate());
    Transaction updatedTransaction = this.buildTransaction(request, existingTransaction);
    this.throwErrorOnExistingReportWithStatusApprovedBySupervisor(
        existingTransaction.getOrganizationRegion().getId(),
        DateUtils.convertTimestampToString(updatedTransaction.getTransactionDate()));
    this.checkMonthlyReport(existingTransaction.getOrganizationRegion(),
        request.getTransactionDate());
    this.transactionRepository.save(updatedTransaction);
    this.walletService.recalculateWalletByGeneralLedgerAccountType(updatedTransaction);
    return updatedTransaction;
  }
  
  private Transaction buildTransaction(UpdateTransactionRequest request,
      Transaction existingTransaction) throws ApplicationException {
    existingTransaction.setName(request.getName());
    existingTransaction
        .setTransactionDate(DateUtils.dateInputToTimestamp(request.getTransactionDate()));
    existingTransaction.setAmount(request.getAmount());
    existingTransaction.setDescription(request.getDescription());
    existingTransaction.setEntryPosition(request.getEntryPosition());
    existingTransaction.setGeneralLedgerAccountType(request.getGeneralLedgerAccountType());
    existingTransaction.setType(request.getType());
    existingTransaction.setProof(request.getProof().getBytes(StandardCharsets.UTF_8));
    return existingTransaction;
  }

  @Override
  public Boolean deleteTransaction(String transactionId) throws ApplicationException {
    Transaction transaction =
        this.transactionRepository.findByIdAndMarkForDeleteFalse(transactionId)
            .orElseThrow(() -> new ApplicationException(HttpStatus.BAD_REQUEST,
                ErrorMessages.TRANSACTION_NOT_FOUND));
    this.checkMonthlyReport(transaction.getOrganizationRegion(),
        DateUtils.convertTimestampToString(transaction.getTransactionDate()));
    this.walletService.recalculateWalletByRemovedTransaction(transaction);
    transaction.setMarkForDelete(true);
    this.transactionRepository.save(transaction);
    return Boolean.TRUE;
  }
}
