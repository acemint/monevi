package com.monevi.repository;

import com.monevi.entity.Report;
import com.monevi.model.GetReportFilter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ReportCustomRepository {

  Page<Report> getReports(GetReportFilter filter);

}
