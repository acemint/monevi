package com.monevi.repository;

import javax.persistence.Tuple;

import org.springframework.data.domain.Page;

import com.monevi.exception.ApplicationException;
import com.monevi.model.GetReportHistoryFilter;

public interface ReportHistoryCustomRepository {

  Page<Tuple> findReportHistoryByRole(GetReportHistoryFilter filter) throws ApplicationException;
}
