package com.monevi.service;

import com.monevi.entity.Report;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetReportFilter;

import java.util.List;

public interface ReportService {

  List<Report> getReports(GetReportFilter filter) throws ApplicationException;

}
