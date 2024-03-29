package com.monevi.service;

import com.monevi.dto.request.SubmitReportRequest;
import com.monevi.dto.request.ReportApproveRequest;
import com.monevi.dto.request.ReportRejectRequest;
import com.monevi.dto.response.ReportSummary;
import com.monevi.entity.Report;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetReportFilter;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReportService {

  Page<Report> getReports(GetReportFilter filter) throws ApplicationException;

  Report get(String id) throws ApplicationException;

  Report submitReport(SubmitReportRequest request) throws ApplicationException;

  Report reject(ReportRejectRequest request) throws ApplicationException;

  Report approve(ReportApproveRequest request) throws ApplicationException;

  ReportSummary summarize(String organizationRegionId, String date) throws ApplicationException;

}
