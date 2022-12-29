package com.monevi.service;

import com.monevi.constant.ErrorMessages;
import com.monevi.dto.request.ReportApproveRequest;
import com.monevi.dto.request.ReportRejectRequest;
import com.monevi.entity.Report;
import com.monevi.entity.ReportComment;
import com.monevi.entity.UserAccount;
import com.monevi.enums.ReportStatus;
import com.monevi.enums.UserAccountRole;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetReportFilter;
import com.monevi.repository.OrganizationRegionRepository;
import com.monevi.repository.ReportRepository;
import com.monevi.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

  @Autowired
  private OrganizationRegionRepository organizationRegionRepository;

  @Autowired
  private ReportRepository reportRepository;

  @Autowired
  private UserAccountRepository userAccountRepository;

  @Override
  public List<Report> getReports(GetReportFilter filter) throws ApplicationException {
    this.organizationRegionRepository.findByIdAndMarkForDeleteIsFalse(filter.getOrganizationRegionId())
        .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.ORGANIZATION_REGION_DOES_NOT_EXISTS));
    return this.reportRepository.getReports(filter).orElse(Collections.emptyList());
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

}
