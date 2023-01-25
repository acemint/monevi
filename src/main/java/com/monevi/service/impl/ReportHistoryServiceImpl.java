package com.monevi.service.impl;

import com.monevi.constant.ErrorMessages;
import com.monevi.dto.vo.ReportHistoryFindResponseVO;
import com.monevi.entity.Report;
import com.monevi.entity.UserAccount;
import com.monevi.enums.ReportStatus;
import com.monevi.enums.UserAccountRole;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetReportHistoryFilter;
import com.monevi.repository.ReportRepository;
import com.monevi.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.monevi.entity.ReportHistory;
import com.monevi.repository.ReportHistoryRepository;
import com.monevi.service.ReportHistoryService;

import javax.persistence.Tuple;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportHistoryServiceImpl implements ReportHistoryService {

  @Autowired
  private ReportHistoryRepository reportHistoryRepository;
  
  @Autowired
  private UserAccountRepository userAccountRepository;
  
  @Autowired
  private ReportRepository reportRepository;

  @Override
  public ReportHistory createReportHistory(String userId, String reportId)
      throws ApplicationException {
    Report report = this.reportRepository.findByIdAndMarkForDeleteIsFalse(reportId)
        .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorMessages.REPORT_DOES_NOT_EXIST));

    UserAccount user = this.userAccountRepository.findByIdAndMarkForDeleteIsFalse(userId)
        .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorMessages.USER_ACCOUNT_NOT_FOUND));

    ReportHistory newReportHistory =
        ReportHistory.builder().report(report)
            .user(user)
            .remarks(report.getStatus())
            .build();
    return this.reportHistoryRepository.save(newReportHistory);
  }

  @Override
  public Page<ReportHistoryFindResponseVO> findAllReportHistory(String userId, Pageable pageable)
      throws ApplicationException {
    UserAccount userAccount = this.userAccountRepository.findByIdAndMarkForDeleteIsFalse(userId)
        .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorMessages.USER_ACCOUNT_NOT_FOUND));

    GetReportHistoryFilter filter = GetReportHistoryFilter.builder().userId(userId)
        .pageable(pageable).userRole(userAccount.getRole()).build();
    if (UserAccountRole.CHAIRMAN.equals(userAccount.getRole())
        || UserAccountRole.TREASURER.equals(userAccount.getRole())) {
      filter.setOrganizationRegionId(userAccount.getOrganizationRegion().getId());
      filter.setTermOfOffice(userAccount.getPeriodYear());
    } else if (UserAccountRole.SUPERVISOR.equals(userAccount.getRole())) {
      filter.setRegionId(userAccount.getRegion().getId());
    } else {
      throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.INVALID_ROLE);
    }
    
    Page<Tuple> result = this.reportHistoryRepository.findReportHistoryByUser(filter);
    List<ReportHistoryFindResponseVO> historyVOList = result.getContent().stream()
        .map(this::toReportHistoryFindResponseVO)
        .collect(Collectors.toList());
    return new PageImpl<>(historyVOList, result.getPageable(), result.getTotalElements());
  }
  
  private ReportHistoryFindResponseVO toReportHistoryFindResponseVO(Tuple data) {
    return ReportHistoryFindResponseVO.builder()
        .organizationName(data.get(0).toString())
        .reportPeriod(((Timestamp) data.get(1)))
        .remarks(((ReportStatus) data.get(2)))
        .userName(data.get(3).toString())
        .createdDate(((Timestamp) data.get(4))).build();
  }
}
