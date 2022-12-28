package com.monevi.repository;

import com.monevi.entity.Report;
import com.monevi.model.GetReportFilter;

import java.util.List;
import java.util.Optional;

public interface ReportCustomRepository {

  Optional<List<Report>> getReports(GetReportFilter filter);

}
