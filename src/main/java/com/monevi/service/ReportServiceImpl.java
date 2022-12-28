package com.monevi.service;

import com.monevi.constant.ErrorMessages;
import com.monevi.dto.request.ReportRejectRequest;
import com.monevi.entity.Report;
import com.monevi.entity.ReportComment;
import com.monevi.enums.ReportStatus;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetReportFilter;
import com.monevi.repository.OrganizationRegionRepository;
import com.monevi.repository.ReportRepository;
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

  @Override
  public List<Report> getReports(GetReportFilter filter) throws ApplicationException {
    this.organizationRegionRepository.findByIdAndMarkForDeleteIsFalse(filter.getOrganizationRegionId())
        .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.ORGANIZATION_REGION_DOES_NOT_EXISTS));
    return this.reportRepository.getReports(filter).orElse(Collections.emptyList());
  }

  @Override
  public Report reject(ReportRejectRequest request) throws ApplicationException {
      /*
      TODO: Given a user id, find the username of the person. For now set commented by as the user id (MEDIUM)
      TODO: Validate that only Student with Leader Role and Supervisor can do this (LOW). This will also be done in front end
       */
    Report report = this.reportRepository.findByIdAndMarkForDeleteIsFalse(request.getReportId())
        .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.REPORT_DOES_NOT_EXIST));
    ReportComment reportComment = this.buildReportComment(report, request.getUserId(), request.getComment());
    report.setReportComment(reportComment);
    report.setStatus(ReportStatus.UNAPPROVED);
    return this.reportRepository.save(report);
  }

  private ReportComment buildReportComment(Report report, String commentedBy, String comment) {
    return ReportComment.builder()
        .report(report)
        .commentedBy(commentedBy)
        .content(comment)
        .build();
  }

}
