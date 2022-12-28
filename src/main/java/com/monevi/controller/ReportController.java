package com.monevi.controller;

import com.monevi.converter.Converter;
import com.monevi.converter.ReportToReportResponseConverter;
import com.monevi.dto.request.ReportRejectRequest;
import com.monevi.dto.response.BaseResponse;
import com.monevi.dto.response.MultipleBaseResponse;
import com.monevi.dto.response.ReportResponse;
import com.monevi.entity.Report;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetReportFilter;
import com.monevi.service.ReportService;
import com.monevi.util.ReportUrlUtils;
import com.monevi.validation.annotation.ValidDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ApiPath.BASE + ApiPath.REPORT)
public class ReportController {

  @Autowired
  private ReportService reportService;

  @Autowired
  @Qualifier(ReportToReportResponseConverter.REPORT_TO_REPORT_RESPONSE_BEAN_NAME + Converter.SUFFIX_BEAN_NAME)
  private Converter<Report, ReportResponse> reportToReportResponseConverter;

  @GetMapping(value = ApiPath.FIND_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
  public MultipleBaseResponse<ReportResponse> getReports(
      @RequestParam String organizationRegionId,
      @RequestParam @ValidDate String startDate,
      @RequestParam @ValidDate String endDate,
      @RequestParam(defaultValue = "0", required = false) int page,
      @RequestParam(defaultValue = "1000", required = false) int size,
      @RequestParam(defaultValue = "name", required = false) String[] sortBy,
      @RequestParam(defaultValue = "true", required = false) String[] isAscending) throws ApplicationException {
    GetReportFilter filter = this.buildDefaultGetReportsFilter(
        organizationRegionId, startDate,endDate, sortBy, isAscending, page, size);
    List<ReportResponse> reportResponses = this.reportService.getReports(filter)
        .stream()
        .map(r -> this.reportToReportResponseConverter.convert(r))
        .collect(Collectors.toList());
    return MultipleBaseResponse.<ReportResponse>builder()
        .values(reportResponses)
        .metadata(MultipleBaseResponse.Metadata
            .builder()
            .size(reportResponses.size())
            .totalPage(0)
            .totalItems(reportResponses.size())
            .build())
        .build();
  }

  @PostMapping(value = ApiPath.REJECT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<ReportResponse> reject(
      @Valid @RequestBody ReportRejectRequest request) throws ApplicationException {
    Report report = this.reportService.reject(request);
    return BaseResponse.<ReportResponse>builder()
        .value(this.reportToReportResponseConverter.convert(report))
        .build();
  }

  private GetReportFilter buildDefaultGetReportsFilter(
      String organizationRegionId, String startDate, String endDate,
      String[] sortBy, String[] isAscending, int page, int size) throws ApplicationException {
    List<Sort.Order> sortOrders = new ArrayList<>();
    int validSize = Math.min(sortBy.length, isAscending.length);
    for (int i = 0; i < validSize; i++) {
      ReportUrlUtils.checkValidSortedBy(sortBy[i]);
      sortOrders.add(new Sort.Order(ReportUrlUtils.getSortDirection(isAscending[i]), sortBy[i]));
    }
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortOrders));
    return GetReportFilter.builder()
        .organizationRegionId(organizationRegionId)
        .startDate(startDate)
        .endDate(endDate)
        .pageable(pageable)
        .build();
  }


}
