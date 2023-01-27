package com.monevi.service;

import javax.persistence.Tuple;

import com.monevi.dto.vo.ReportHistoryFindResponseVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.monevi.entity.ReportHistory;
import com.monevi.exception.ApplicationException;

public interface ReportHistoryService {

  ReportHistory createReportHistory(String userId, String reportId) throws ApplicationException;

  Page<ReportHistoryFindResponseVO> findAllReportHistory(String userId, Pageable pageable)
      throws ApplicationException;
}
