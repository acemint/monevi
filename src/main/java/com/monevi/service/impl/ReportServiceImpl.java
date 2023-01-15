package com.monevi.service.impl;

import com.monevi.constant.ErrorMessages;
import com.monevi.dto.request.SubmitReportRequest;
import com.monevi.dto.request.ReportApproveRequest;
import com.monevi.dto.request.ReportRejectRequest;
import com.monevi.entity.BaseEntity;
import com.monevi.entity.OrganizationRegion;
import com.monevi.entity.Report;
import com.monevi.entity.ReportComment;
import com.monevi.entity.ReportGeneralLedgerAccount;
import com.monevi.entity.Transaction;
import com.monevi.entity.UserAccount;
import com.monevi.enums.EntryPosition;
import com.monevi.enums.GeneralLedgerAccountType;
import com.monevi.enums.ReportStatus;
import com.monevi.enums.UserAccountRole;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetReportFilter;
import com.monevi.model.GetTransactionFilter;
import com.monevi.repository.OrganizationRegionRepository;
import com.monevi.repository.ReportRepository;
import com.monevi.repository.TransactionRepository;
import com.monevi.repository.UserAccountRepository;
import com.monevi.service.ReportService;
import com.monevi.util.DateUtils;
import com.monevi.util.FinanceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

  @Autowired
  private OrganizationRegionRepository organizationRegionRepository;

  @Autowired
  private ReportRepository reportRepository;

  @Autowired
  private UserAccountRepository userAccountRepository;

  @Autowired
  private TransactionRepository transactionRepository;

  @Override
  public List<Report> getReports(GetReportFilter filter) throws ApplicationException {
    this.organizationRegionRepository.findByIdAndMarkForDeleteIsFalse(filter.getOrganizationRegionId())
        .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.ORGANIZATION_REGION_DOES_NOT_EXISTS));
    return this.reportRepository.getReports(filter).orElse(Collections.emptyList());
  }

  @Override
  public Report submitReport(SubmitReportRequest request) throws ApplicationException {
    OrganizationRegion organizationRegion = this.organizationRegionRepository.findByIdAndMarkForDeleteIsFalse(request.getOrganizationRegionId())
        .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.ORGANIZATION_REGION_DOES_NOT_EXISTS));
    this.deleteExistingCurrentMonthReport(request.getOrganizationRegionId(), request.getDate());

    List<Transaction> transactions = this.getCurrentMonthTransactions(
        organizationRegion.getId(), request.getDate());
    Report previousMonthReport = this.getLastMonthReport(
        organizationRegion.getId(), request.getDate());
    Report newReport = this.buildNewReport(organizationRegion, transactions, previousMonthReport, request.getDate());

    transactions.forEach(t -> t.setReport(newReport));
    this.transactionRepository.saveAll(transactions);

    return this.reportRepository.save(newReport);
  }

  @Override
  public Report reject(ReportRejectRequest request) throws ApplicationException {
    Report report = this.reportRepository.findByIdAndMarkForDeleteIsFalse(request.getReportId())
        .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.REPORT_DOES_NOT_EXIST));
    UserAccount userAccount = this.userAccountRepository.findByIdAndMarkForDeleteIsFalse(request.getUserId())
        .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.USER_ACCOUNT_DOES_NOT_EXIST));
    this.throwErrorOnInvalidReportHandlingByUser(report, userAccount);
    this.rejectReport(report, userAccount.getFullName(), request.getComment());
    return this.reportRepository.save(report);
  }

  @Override
  public Report approve(ReportApproveRequest request) throws ApplicationException {
    Report report = this.reportRepository.findByIdAndMarkForDeleteIsFalse(request.getReportId())
        .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.REPORT_DOES_NOT_EXIST));
    UserAccount userAccount = this.userAccountRepository.findByIdAndMarkForDeleteIsFalse(request.getUserId())
        .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.USER_ACCOUNT_DOES_NOT_EXIST));
    this.throwErrorOnInvalidReportHandlingByUser(report, userAccount);
    this.approveReport(report);
    return this.reportRepository.save(report);
  }

  private void throwErrorOnInvalidReportHandlingByUser(Report report, UserAccount userAccount) throws ApplicationException {
    if (userAccount.getRole().equals(UserAccountRole.SUPERVISOR)) {
      List<String> userOrganizationRegionIds = userAccount.getRegion().getOrganizationRegions()
          .stream().map(BaseEntity::getId).collect(Collectors.toList());
      if (!userOrganizationRegionIds.contains(report.getOrganizationRegion().getId())) {
        throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.REPORT_HANDLING_IS_PROHIBITED);
      }
    }
    if (userAccount.getRole().equals(UserAccountRole.CHAIRMAN)) {
      if (!userAccount.getOrganizationRegion().getId().equals(
          report.getOrganizationRegion().getId())) {
        throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.REPORT_HANDLING_IS_PROHIBITED);
      }
    }

    if (report.getStatus().equals(ReportStatus.UNAPPROVED)
        && !userAccount.getRole().equals(UserAccountRole.CHAIRMAN)) {
      throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.REPORT_HANDLING_IS_PROHIBITED);
    }
    if (report.getStatus().equals(ReportStatus.APPROVED_BY_CHAIRMAN)
        && !userAccount.getRole().equals(UserAccountRole.SUPERVISOR)) {
      throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.REPORT_HANDLING_IS_PROHIBITED);
    }
    
    if (report.getStatus().equals(ReportStatus.APPROVED_BY_SUPERVISOR)) {
      throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.REPORT_HANDLING_IS_PROHIBITED);
    }
  }

  private void rejectReport(Report report, String fullName, String comment) {
    ReportComment reportComment = this.buildReportComment(report, fullName, comment);
    report.setReportComment(reportComment);
    report.setStatus(ReportStatus.UNAPPROVED);
  }

  private ReportComment buildReportComment(Report report, String commentedBy, String comment) {
    return ReportComment.builder()
        .report(report)
        .commentedBy(commentedBy)
        .content(comment)
        .build();
  }

  private void approveReport(Report report) {
    if (report.getStatus().equals(ReportStatus.UNAPPROVED)) {
      report.setStatus(ReportStatus.APPROVED_BY_CHAIRMAN);
    }
    else if (report.getStatus().equals(ReportStatus.APPROVED_BY_CHAIRMAN)) {
      report.setStatus(ReportStatus.APPROVED_BY_SUPERVISOR);
    }
    report.getReportComment().setMarkForDelete(true);
  }

  private void deleteExistingCurrentMonthReport(String organizationRegionId, String date) throws ApplicationException {
    GetReportFilter filter = GetReportFilter.builder()
        .organizationRegionId(organizationRegionId)
        .startDate(DateUtils.dateToFirstDayOfMonth(date))
        .endDate(DateUtils.dateToLastDayOfMonth(date))
        .build();
    List<Report> reports = this.reportRepository.getReports(filter).orElse(Collections.emptyList());
    if (reports.size() != 0) {
      if (!reports.get(0).getStatus().equals(ReportStatus.UNAPPROVED)) {
        throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.REPORT_HANDLING_IS_PROHIBITED);
      }
      reports.get(0).setMarkForDelete(true);
      this.reportRepository.save(reports.get(0));
    }
  }

  private Report buildNewReport(
      OrganizationRegion organizationRegion,
      List<Transaction> currentMonthTransactions,
      Report previousMonthReport,
      String date) throws ApplicationException {

    Report newReport = Report.builder()
        .periodDate(DateUtils.dateInputToTimestamp(DateUtils.dateToFirstDayOfMonth(date)))
        .status(ReportStatus.UNAPPROVED)
        .organizationRegion(organizationRegion)
        .reportGeneralLedgerAccounts(Collections.emptySet())
        .build();

    Map<String, Double> generalLedgerAccountAmounts = new HashMap<>();
    this.sumTransactionAmountsByGeneralLedgerAccount(generalLedgerAccountAmounts, currentMonthTransactions);
    this.sumWithPreviousMonthGeneralLedgerAccount(generalLedgerAccountAmounts, previousMonthReport);
    this.buildGeneralLedgerAccounts(newReport, generalLedgerAccountAmounts);
    return this.reportRepository.save(newReport);
  }

  private void sumTransactionAmountsByGeneralLedgerAccount(Map<String, Double> generalLedgerAccountAmounts, List<Transaction> transactions)
      throws ApplicationException {
    for (Transaction transaction : transactions) {
      String generalLedgerName = transaction.getGeneralLedgerAccountType().name();
      if (generalLedgerAccountAmounts.containsKey(generalLedgerName)) {
        Double amount = generalLedgerAccountAmounts.get(generalLedgerName);
        generalLedgerAccountAmounts.replace(
            generalLedgerName,
            amount + FinanceUtils.getActualAmount(transaction.getEntryPosition(), transaction.getAmount()));
      }
      else {
        generalLedgerAccountAmounts.put(
            generalLedgerName,
            FinanceUtils.getActualAmount(transaction.getEntryPosition(), transaction.getAmount()));
      }
    }
  }

  private void sumWithPreviousMonthGeneralLedgerAccount(Map<String, Double> generalLedgerAccountAmounts, Report previousMonthReport) {
    for (ReportGeneralLedgerAccount reportGeneralLedgerAccount : previousMonthReport.getReportGeneralLedgerAccounts()) {
      String generalLedgerName = reportGeneralLedgerAccount.getName().name();
      if (generalLedgerAccountAmounts.containsKey(generalLedgerName)) {
        Double amount = generalLedgerAccountAmounts.get(generalLedgerName);
        generalLedgerAccountAmounts.replace(generalLedgerName, amount + reportGeneralLedgerAccount.getTotal());
      }
      else {
        generalLedgerAccountAmounts.put(generalLedgerName, reportGeneralLedgerAccount.getTotal());
      }
    }
  }

  private void buildGeneralLedgerAccounts(Report report, Map<String, Double> generalLedgerAccountAmounts) {
    Set<ReportGeneralLedgerAccount> reportGeneralLedgerAccounts = new HashSet<>();
    for (Map.Entry<String, Double> generalLedgerAmountName : generalLedgerAccountAmounts.entrySet()) {
      ReportGeneralLedgerAccount reportGeneralLedgerAccount = ReportGeneralLedgerAccount
          .builder()
          .report(report)
          .total(generalLedgerAmountName.getValue())
          .name(GeneralLedgerAccountType.valueOf(generalLedgerAmountName.getKey()))
          .build();
      reportGeneralLedgerAccounts.add(reportGeneralLedgerAccount);
    }
    report.setReportGeneralLedgerAccounts(reportGeneralLedgerAccounts);
  }

  private List<Transaction> getCurrentMonthTransactions(
      String organizationRegionId,
      String date) throws ApplicationException {
    GetTransactionFilter filter = GetTransactionFilter.builder()
        .organizationRegionId(organizationRegionId)
        .startDate(DateUtils.dateToFirstDayOfMonth(date))
        .endDate(DateUtils.dateToLastDayOfMonth(date))
        .build();
    return this.transactionRepository.getTransactions(filter)
        .orElse(Collections.emptyList());

  }

  private Report getLastMonthReport(String organizationRegionId, String date) throws ApplicationException {
    String previousMonthDate = DateUtils.deductMonthFromDate(date, 1);
    GetReportFilter filter = GetReportFilter.builder()
        .organizationRegionId(organizationRegionId)
        .startDate(DateUtils.dateToFirstDayOfMonth(previousMonthDate))
        .endDate(DateUtils.dateToLastDayOfMonth(previousMonthDate))
        .build();
    List<Report> reports = this.reportRepository.getReports(filter).orElse(Collections.emptyList());
    if (reports.size() != 0) {
      Report report = reports.get(0);
      if (!report.getStatus().equals(ReportStatus.APPROVED_BY_SUPERVISOR)) {
        throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.REPORT_HANDLING_IS_PROHIBITED);
      }
      return report;
    }
    return Report.builder().build();
  }

}
