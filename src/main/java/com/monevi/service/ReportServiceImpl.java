package com.monevi.service;

import com.monevi.constant.ErrorMessages;
import com.monevi.entity.Report;
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
}
