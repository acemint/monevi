package com.monevi.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Tuple;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.monevi.constant.ErrorMessages;
import com.monevi.dto.request.ReportApproveRequest;
import com.monevi.dto.request.ReportRejectRequest;
import com.monevi.dto.request.SendEmailRequest;
import com.monevi.dto.request.SubmitReportRequest;
import com.monevi.dto.response.ReportSummary;
import com.monevi.entity.BaseEntity;
import com.monevi.entity.OrganizationRegion;
import com.monevi.entity.Report;
import com.monevi.entity.ReportComment;
import com.monevi.entity.ReportGeneralLedgerAccount;
import com.monevi.entity.Transaction;
import com.monevi.entity.UserAccount;
import com.monevi.enums.EntryPosition;
import com.monevi.enums.GeneralLedgerAccountType;
import com.monevi.enums.MessageTemplate;
import com.monevi.enums.ReportStatus;
import com.monevi.enums.TransactionType;
import com.monevi.enums.UserAccountRole;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetReportFilter;
import com.monevi.model.GetTransactionFilter;
import com.monevi.repository.GeneralLedgerAccountRepository;
import com.monevi.repository.OrganizationRegionRepository;
import com.monevi.repository.RegionRepository;
import com.monevi.repository.ReportCommentRepository;
import com.monevi.repository.ReportRepository;
import com.monevi.repository.TransactionRepository;
import com.monevi.repository.UserAccountRepository;
import com.monevi.service.MessageService;
import com.monevi.service.ReportHistoryService;
import com.monevi.service.ReportService;
import com.monevi.util.DateUtils;
import com.monevi.util.FinanceUtils;

@Service
public class ReportServiceImpl implements ReportService {

  @Value("${monevi.redirect.login.url}")
  private String loginUrl;

  private static final String USER_NAME = "name";
  private static final String URL_KEY = "url";
  private static final String REPORT_MONTH_KEY = "reportMonth";
  private static final String REPORT_YEAR_KEY = "reportYear";
  private static final String ORGANIZATION_NAME_KEY = "organizationName";
  private static final String COMMENT_KEY = "comment";
  private static final String COMMENTED_BY_KEY = "commentedBy";
  private static final String SUPERVISOR_KEY = "supervisorName";

  @Autowired
  private OrganizationRegionRepository organizationRegionRepository;

  @Autowired
  private ReportRepository reportRepository;

  @Autowired
  private UserAccountRepository userAccountRepository;

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private RegionRepository regionRepository;

  @Autowired
  private GeneralLedgerAccountRepository generalLedgerAccountRepository;

  @Autowired
  private ReportCommentRepository reportCommentRepository;

  @Autowired
  private ReportHistoryService reportHistoryService;

  @Autowired
  private MessageService messageService;

  @Override
  public Page<Report> getReports(GetReportFilter filter) throws ApplicationException {
    if (StringUtils.isNotBlank(filter.getOrganizationRegionId())) {
      this.organizationRegionRepository
          .findByIdAndMarkForDeleteIsFalse(filter.getOrganizationRegionId())
          .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
              ErrorMessages.ORGANIZATION_REGION_DOES_NOT_EXISTS));
      if (StringUtils.isBlank(filter.getStartDate())) {
        throw new ApplicationException(HttpStatus.BAD_REQUEST,
            ErrorMessages.START_DATE_MUST_NOT_BE_BLANK);
      }
      if (StringUtils.isBlank(filter.getEndDate())) {
        throw new ApplicationException(HttpStatus.BAD_REQUEST,
            ErrorMessages.END_DATE_MUST_NOT_BE_BLANK);
      }
    } else if (StringUtils.isNotBlank(filter.getRegionId())) {
      this.regionRepository.findByIdAndMarkForDeleteFalse(filter.getRegionId())
          .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
              ErrorMessages.REGION_DOES_NOT_EXIST));
    }
    return this.reportRepository.getReports(filter);
  }

  @Override
  public Report get(String id) throws ApplicationException {
    return this.reportRepository.findByIdAndMarkForDeleteIsFalse(id)
        .orElseThrow(() -> new ApplicationException(HttpStatus.BAD_REQUEST, ErrorMessages.REPORT_DOES_NOT_EXIST));
  }

  @Override
  public Report submitReport(SubmitReportRequest request) throws ApplicationException {
    OrganizationRegion organizationRegion = this.organizationRegionRepository.findByIdAndMarkForDeleteIsFalse(request.getOrganizationRegionId())
        .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.ORGANIZATION_REGION_DOES_NOT_EXISTS));
    this.deleteExistingCurrentMonthReport(request.getOrganizationRegionId(), request.getDate());

    UserAccount userAccount =
        this.userAccountRepository.findByIdAndMarkForDeleteIsFalse(request.getUserId())
            .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorMessages.USER_ACCOUNT_NOT_FOUND));

    List<Transaction> transactions = this.getCurrentMonthTransactions(
        organizationRegion.getId(), request.getDate());
    Optional<Report> previousMonthReport = this.getLastMonthReport(
        organizationRegion.getId(), request.getDate());
    if (previousMonthReport.isPresent()) {
      if (!previousMonthReport.get().getStatus().equals(ReportStatus.APPROVED_BY_SUPERVISOR)) {
        throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.REPORT_HANDLING_IS_PROHIBITED);
      }
    }
    Report newReport = this.buildNewReport(organizationRegion, transactions,
        previousMonthReport.orElse(Report.builder().build()), request.getDate(),
        userAccount.getPeriodYear(), request.getOpnameData());

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
    this.reportHistoryService.createReportHistory(userAccount.getId(), report.getId());
    this.sendEmailToTreasurer(report, MessageTemplate.DECLINED_REPORT, null, userAccount.getRole());
    return this.reportRepository.save(report);
  }

  @Override
  public Report approve(ReportApproveRequest request) throws ApplicationException {
    Report report = this.reportRepository.findByIdAndMarkForDeleteIsFalse(request.getReportId())
        .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.REPORT_DOES_NOT_EXIST));
    UserAccount userAccount = this.userAccountRepository.findByIdAndMarkForDeleteIsFalse(request.getUserId())
        .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.USER_ACCOUNT_DOES_NOT_EXIST));
    this.throwErrorOnInvalidReportHandlingByUser(report, userAccount);
    this.approveReport(report, userAccount);
    this.reportHistoryService.createReportHistory(userAccount.getId(), report.getId());
    return this.reportRepository.save(report);
  }

  @Override
  public ReportSummary summarize(String organizationRegionId, String date) throws ApplicationException {
    Report currentMonthReport = this.getCurrentMonthReport(organizationRegionId, date)
        .orElseThrow(() -> new ApplicationException(HttpStatus.BAD_REQUEST, ErrorMessages.REPORT_DOES_NOT_EXIST));
    List<Tuple> transactionSummaryData = this.transactionRepository.groupingTransactionForReportDisplay(currentMonthReport.getId());
    Report lastMonthReport = this.getLastMonthReport(organizationRegionId, date).orElse(Report.builder().build());
    return this.buildReportSummary(transactionSummaryData, currentMonthReport, lastMonthReport);
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

    if (report.getStatus().equals(ReportStatus.NOT_SENT)
        && !userAccount.getRole().equals(UserAccountRole.TREASURER)) {
      throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.REPORT_HANDLING_IS_PROHIBITED);
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
    if (report.getStatus().equals(ReportStatus.DECLINED)) {
      throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
          ErrorMessages.REPORT_HANDLING_IS_PROHIBITED);
    }
  }

  private void rejectReport(Report report, String fullName, String comment) {
    ReportComment reportComment = this.buildReportComment(report, fullName, comment);
    report.setReportComment(reportComment);
    report.setStatus(ReportStatus.DECLINED);
  }

  private ReportComment buildReportComment(Report report, String commentedBy, String comment) {
    return ReportComment.builder()
        .report(report)
        .commentedBy(commentedBy)
        .content(comment)
        .build();
  }

  private void approveReport(Report report, UserAccount user) throws ApplicationException {
    if (report.getStatus().equals(ReportStatus.NOT_SENT)) {
      this.validateOpnameAndTransactionsData(report);
      report.setStatus(ReportStatus.UNAPPROVED);
      this.sendEmailToChairman(report, user);
    }
    else if (report.getStatus().equals(ReportStatus.UNAPPROVED)) {
      report.setStatus(ReportStatus.APPROVED_BY_CHAIRMAN);
      this.sendEmailToSupervisor(report, user);
    }
    else if (report.getStatus().equals(ReportStatus.APPROVED_BY_CHAIRMAN)) {
      report.setStatus(ReportStatus.APPROVED_BY_SUPERVISOR);
      this.sendEmailToTreasurer(report, MessageTemplate.APPROVED_REPORT, user.getFullName(), null);
    }
    if (Objects.nonNull(report.getReportComment())) {
      report.getReportComment().setMarkForDelete(true);
    }
  }

  private void validateOpnameAndTransactionsData(Report report) throws ApplicationException {
    for (ReportGeneralLedgerAccount data : report.getReportGeneralLedgerAccounts()) {
      if (data.getOpname() != data.getTotal()) {
        throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
            String.format(ErrorMessages.TOTAL_AND_OPNAME_NOT_MATCH, data.getName().getExcelValue()));
      }
    }
  }

  private void sendEmailToChairman(Report report, UserAccount treasurerAccount)
      throws ApplicationException {
    String organizationRegionId = report.getOrganizationRegion().getId();
    UserAccount recipient = this.userAccountRepository
        .findAssignedUserByOrganizationRegionIdAndRoleAndMarkForDeleteFalse(
            treasurerAccount.getPeriodMonth(), treasurerAccount.getPeriodYear(),
            report.getOrganizationRegion().getId(), UserAccountRole.CHAIRMAN)
        .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorMessages.USER_ACCOUNT_NOT_FOUND));
    String organizationName =
        this.organizationRegionRepository.findByIdAndMarkForDeleteIsFalse(organizationRegionId)
            .map(data -> data.getOrganization().getAbbreviation())
            .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorMessages.ORGANIZATION_DOES_NOT_EXIST));
    String reportMonth = String.valueOf(report.getPeriodDate().toLocalDateTime().getMonth());
    Map<String, String> variables = new HashMap<>();
    variables.put(USER_NAME, StringUtils.split(recipient.getFullName(), " ")[0]);
    variables.put(REPORT_MONTH_KEY, reportMonth);
    variables.put(REPORT_YEAR_KEY, String.valueOf(report.getPeriodDate().toLocalDateTime().getYear()));
    variables.put(ORGANIZATION_NAME_KEY, organizationName);
    variables.put(URL_KEY, loginUrl);
    SendEmailRequest request =
        SendEmailRequest.builder()
            .messageTemplateId(MessageTemplate.SUBMITTED_REPORT)
            .recipient(recipient.getEmail())
            .variables(variables).build();
    this.messageService.sendEmail(request);
  }

  private void sendEmailToSupervisor(Report report, UserAccount chairmanAccount)
      throws ApplicationException {
    List<String> recipients = this.userAccountRepository
        .findAllByRoleAndRegionAndMarkForDeleteFalse(UserAccountRole.SUPERVISOR,
            chairmanAccount.getOrganizationRegion().getRegion())
            .map(data -> data.stream()
                    .map(UserAccount::getEmail).collect(Collectors.toList()))
        .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorMessages.USER_ACCOUNT_NOT_FOUND));
    String recipientEmails = String.join(",", recipients);
    String organizationName = this.organizationRegionRepository
        .findByIdAndMarkForDeleteIsFalse(chairmanAccount.getOrganizationRegion().getId())
        .map(data -> data.getOrganization().getAbbreviation())
        .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorMessages.ORGANIZATION_DOES_NOT_EXIST));
    String reportMonth = String.valueOf(report.getPeriodDate().toLocalDateTime().getMonth());
    Map<String, String> variables = new HashMap<>();
    variables.put(USER_NAME, "Bapak/Ibu");
    variables.put(REPORT_MONTH_KEY, reportMonth);
    variables.put(REPORT_YEAR_KEY, String.valueOf(report.getPeriodDate().toLocalDateTime().getYear()));
    variables.put(ORGANIZATION_NAME_KEY, organizationName);
    variables.put(URL_KEY, loginUrl);
    SendEmailRequest request =
        SendEmailRequest.builder()
            .messageTemplateId(MessageTemplate.SUBMITTED_REPORT)
            .recipient(recipientEmails)
            .variables(variables).build();
    this.messageService.sendEmail(request);
  }

  private void sendEmailToTreasurer(Report report, MessageTemplate template, String approvedBy, UserAccountRole rejectedBy)
      throws ApplicationException {
    UserAccount recipient = this.userAccountRepository
        .findAssignedUserByOrganizationRegionIdAndRoleAndMarkForDeleteFalse(null,
            report.getTermOfOffice(), report.getOrganizationRegion().getId(),
            UserAccountRole.TREASURER)
        .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorMessages.USER_ACCOUNT_NOT_FOUND));
    String reportMonth = String.valueOf(report.getPeriodDate().toLocalDateTime().getMonth());
    Map<String, String> variables = new HashMap<>();
    variables.put(USER_NAME, StringUtils.split(recipient.getFullName(), " ")[0]);
    variables.put(REPORT_MONTH_KEY, reportMonth);
    variables.put(REPORT_YEAR_KEY, String.valueOf(report.getPeriodDate().toLocalDateTime().getYear()));

    if (MessageTemplate.DECLINED_REPORT.equals(template)) {
      String honorfic = UserAccountRole.SUPERVISOR.equals(rejectedBy) ? "Bapak/Ibu " : "";
      variables.put(COMMENT_KEY, report.getReportComment().getContent());
      variables.put(COMMENTED_BY_KEY, honorfic + report.getReportComment().getCommentedBy());
    } else {
      variables.put(SUPERVISOR_KEY, StringUtils.split(approvedBy, " ")[0]);
    }

    SendEmailRequest request =
        SendEmailRequest.builder()
            .messageTemplateId(template)
            .recipient(recipient.getEmail())
            .variables(variables).build();
    this.messageService.sendEmail(request);
  }

  private void deleteExistingCurrentMonthReport(String organizationRegionId, String date) throws ApplicationException {
    GetReportFilter filter = GetReportFilter.builder()
        .organizationRegionId(organizationRegionId)
        .startDate(DateUtils.dateToFirstDayOfMonth(date))
        .endDate(DateUtils.dateToLastDayOfMonth(date))
        .pageable(PageRequest.of(0, Integer.MAX_VALUE))
        .build();
    List<Report> reports = this.reportRepository.getReports(filter).getContent();
    if (reports.size() != 0) {
      if (reports.get(0).getStatus().equals(ReportStatus.APPROVED_BY_SUPERVISOR) ||
          reports.get(0).getStatus().equals(ReportStatus.APPROVED_BY_CHAIRMAN)) {
        throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.REPORT_HANDLING_IS_PROHIBITED);
      }
      reports.get(0).setMarkForDelete(true);
      this.reportRepository.save(reports.get(0));

      List<ReportGeneralLedgerAccount> generalLedgerAccounts = this.generalLedgerAccountRepository
          .findAllByReportAndMarkForDeleteFalse(reports.get(0)).orElse(null);
      if (Objects.nonNull(generalLedgerAccounts)) {
        generalLedgerAccounts = generalLedgerAccounts.stream().map(generalLedgerAccount -> {
          generalLedgerAccount.setMarkForDelete(true);
          return generalLedgerAccount;
        }).collect(Collectors.toList());
        this.generalLedgerAccountRepository.saveAll(generalLedgerAccounts);
      }

      ReportComment comment = this.reportCommentRepository
          .findByReportAndMarkForDeleteFalse(reports.get(0)).orElse(null);
      if (Objects.nonNull(comment)) {
        comment.setMarkForDelete(true);
        this.reportCommentRepository.save(comment);
      }
    }
  }

  private Report buildNewReport(
      OrganizationRegion organizationRegion,
      List<Transaction> currentMonthTransactions,
      Report previousMonthReport,
      String date,
      Integer termOfOffice,
      Map<GeneralLedgerAccountType, Double> opnameData) throws ApplicationException {

    Report newReport = Report.builder()
        .periodDate(DateUtils.dateInputToTimestamp(DateUtils.dateToFirstDayOfMonth(date)))
        .status(ReportStatus.NOT_SENT)
        .organizationRegion(organizationRegion)
        .reportGeneralLedgerAccounts(Collections.emptySet())
        .termOfOffice(termOfOffice)
        .build();

    Map<GeneralLedgerAccountType, Double> generalLedgerAccountAmounts = new HashMap<>();
    this.sumTransactionAmountsByGeneralLedgerAccount(generalLedgerAccountAmounts, currentMonthTransactions);
    this.sumWithPreviousMonthGeneralLedgerAccount(generalLedgerAccountAmounts, previousMonthReport);

    this.buildGeneralLedgerAccounts(newReport, generalLedgerAccountAmounts, opnameData);
    return this.reportRepository.save(newReport);
  }

  private void sumTransactionAmountsByGeneralLedgerAccount(Map<GeneralLedgerAccountType, Double> generalLedgerAccountAmounts, List<Transaction> transactions)
      throws ApplicationException {
    for (Transaction transaction : transactions) {
      if (generalLedgerAccountAmounts.containsKey(transaction.getGeneralLedgerAccountType())) {
        Double amount = generalLedgerAccountAmounts.get(transaction.getGeneralLedgerAccountType());
        generalLedgerAccountAmounts.replace(
            transaction.getGeneralLedgerAccountType(),
            amount + FinanceUtils.getActualAmount(transaction.getEntryPosition(), transaction.getAmount()));
      }
      else {
        generalLedgerAccountAmounts.put(
            transaction.getGeneralLedgerAccountType(),
            FinanceUtils.getActualAmount(transaction.getEntryPosition(), transaction.getAmount()));
      }
    }
  }

  private void sumWithPreviousMonthGeneralLedgerAccount(Map<GeneralLedgerAccountType, Double> generalLedgerAccountAmounts, Report previousMonthReport) {
    for (ReportGeneralLedgerAccount reportGeneralLedgerAccount : previousMonthReport.getReportGeneralLedgerAccounts()) {
      if (generalLedgerAccountAmounts.containsKey(reportGeneralLedgerAccount.getName())) {
        Double amount = generalLedgerAccountAmounts.get(reportGeneralLedgerAccount.getName());
        generalLedgerAccountAmounts.replace(reportGeneralLedgerAccount.getName(), amount + reportGeneralLedgerAccount.getTotal());
      }
      else {
        generalLedgerAccountAmounts.put(reportGeneralLedgerAccount.getName(), reportGeneralLedgerAccount.getTotal());
      }
    }
  }

  private void buildGeneralLedgerAccounts(Report report, Map<GeneralLedgerAccountType, Double> generalLedgerData, Map<GeneralLedgerAccountType, Double> opnameData) {
    Set<ReportGeneralLedgerAccount> reportGeneralLedgerAccounts = new HashSet<>();
    for (Map.Entry<GeneralLedgerAccountType, Double> generalLedgerDatum : generalLedgerData.entrySet()) {
      ReportGeneralLedgerAccount reportGeneralLedgerAccount = ReportGeneralLedgerAccount
          .builder()
          .report(report)
          .total(generalLedgerDatum.getValue())
          .name(generalLedgerDatum.getKey())
          .build();
      Double opnameAmount = opnameData.get(generalLedgerDatum.getKey());
      if (opnameAmount != null) {
        reportGeneralLedgerAccount.setOpname(opnameAmount);
      }
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
        .pageable(PageRequest.of(0, Integer.MAX_VALUE))
        .build();
    return this.transactionRepository.getTransactions(filter).getContent();
  }

  private Optional<Report> getCurrentMonthReport(String organizationRegionId, String date) throws ApplicationException {
    GetReportFilter filter = GetReportFilter.builder()
        .organizationRegionId(organizationRegionId)
        .startDate(DateUtils.dateToFirstDayOfMonth(date))
        .endDate(DateUtils.dateToLastDayOfMonth(date))
        .pageable(PageRequest.of(0, Integer.MAX_VALUE))
        .build();
    List<Report> reports = this.reportRepository.getReports(filter).getContent();
    if (reports.size() != 0) {
      return Optional.ofNullable(reports.get(0));
    }
    return Optional.empty();
  }

  private Optional<Report> getLastMonthReport(String organizationRegionId, String date) throws ApplicationException {
    String previousMonthDate = DateUtils.deductMonthFromDate(date, 1);
    GetReportFilter filter = GetReportFilter.builder()
        .organizationRegionId(organizationRegionId)
        .startDate(DateUtils.dateToFirstDayOfMonth(previousMonthDate))
        .endDate(DateUtils.dateToLastDayOfMonth(previousMonthDate))
        .pageable(PageRequest.of(0, Integer.MAX_VALUE))
        .build();
    List<Report> reports = this.reportRepository.getReports(filter).getContent();
    if (reports.size() != 0) {
      return Optional.ofNullable(reports.get(0));
    }
    return Optional.empty();
  }

  private ReportSummary buildReportSummary(List<Tuple> transactionSummaryData, Report currentMonthReport, Report lastMonthReport) {
    ReportSummary reportSummary = ReportSummary.builder()
        .reportStatus(currentMonthReport.getStatus())
        .reportId(currentMonthReport.getId())
        .build();
    if (reportSummary.getReportStatus().equals(ReportStatus.DECLINED)) {
      reportSummary.setComment(currentMonthReport.getReportComment().getContent());
      reportSummary.setCommentedBy(currentMonthReport.getReportComment().getCommentedBy());
    }

    for (GeneralLedgerAccountType generalLedgerAccountType : GeneralLedgerAccountType.values()) {
      reportSummary.getGeneralLedgerAccountTypeData().put(generalLedgerAccountType, ReportSummary.GeneralLedgerData
          .builder()
          .build());
      ReportSummary.GeneralLedgerData
          generalLedgerData = reportSummary.getGeneralLedgerAccountTypeData().get(generalLedgerAccountType);

      for (TransactionType transactionType : TransactionType.values()) {
        generalLedgerData.getTransactionTypeData().put(transactionType, ReportSummary.TransactionTypeData
            .builder()
            .build());
        ReportSummary.TransactionTypeData
            transactionTypeData = generalLedgerData.getTransactionTypeData().get(transactionType);

        for (EntryPosition entryPosition : EntryPosition.values()) {
          transactionTypeData.getEntryPositionData().put(entryPosition, ReportSummary.EntryPositionData
              .builder()
              .build());
        }
      }
    }

    for (Tuple transactionSummaryDatum : transactionSummaryData) {
      BigDecimal amount = transactionSummaryDatum.get("amount", BigDecimal.class);
      EntryPosition entryPosition = EntryPosition.valueOf(transactionSummaryDatum.get("entry_position", String.class));
      GeneralLedgerAccountType generalLedgerAccountType = GeneralLedgerAccountType.valueOf(transactionSummaryDatum.get("general_ledger_account_type", String.class));
      TransactionType transactionType = TransactionType.valueOf(transactionSummaryDatum.get("type", String.class));

      ReportSummary.GeneralLedgerData generalLedgerData = reportSummary
          .getGeneralLedgerAccountTypeData().get(generalLedgerAccountType);
      ReportGeneralLedgerAccount currentMonthReportGeneralLedgerAccount = currentMonthReport.getReportGeneralLedgerAccounts()
          .stream().filter(r -> r.getName().equals(generalLedgerAccountType)).findFirst()
          .orElse(ReportGeneralLedgerAccount.builder().opname(Double.valueOf(0)).build());
      generalLedgerData.setOpnameAmount(currentMonthReportGeneralLedgerAccount.getOpname());

      ReportSummary.TransactionTypeData transactionTypeData = generalLedgerData
          .getTransactionTypeData().get(transactionType);

      ReportSummary.EntryPositionData entryPositionData = transactionTypeData
          .getEntryPositionData().get(entryPosition);
      entryPositionData.setAmount(amount.doubleValue());
    }
    
    for (GeneralLedgerAccountType type : GeneralLedgerAccountType.values()) {
      ReportSummary.GeneralLedgerData generalLedgerData =
          reportSummary.getGeneralLedgerAccountTypeData().get(type);
      ReportGeneralLedgerAccount lastMonthReportGeneralLedgerAccount =
          lastMonthReport.getReportGeneralLedgerAccounts().stream()
              .filter(r -> r.getName().equals(type)).findFirst()
              .orElse(ReportGeneralLedgerAccount.builder().opname(Double.valueOf(0)).build());
      generalLedgerData.setPreviousMonthBalance(lastMonthReportGeneralLedgerAccount.getTotal());
    }
    return reportSummary;
  }

}
