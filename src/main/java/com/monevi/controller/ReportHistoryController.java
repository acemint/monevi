package com.monevi.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.monevi.converter.Converter;
import com.monevi.dto.response.MultipleBaseResponse;
import com.monevi.dto.response.ReportHistoryFindResponse;
import com.monevi.dto.vo.ReportHistoryFindResponseVO;
import com.monevi.exception.ApplicationException;
import com.monevi.service.ReportHistoryService;

@RestController
@RequestMapping(ApiPath.BASE + ApiPath.HISTORY)
@Validated
public class ReportHistoryController {

  private static final String SORT_BY_CREATED_DATE = "createdDate";

  @Autowired
  private ReportHistoryService reportHistoryService;
  
  @Autowired
  private Converter<ReportHistoryFindResponseVO, ReportHistoryFindResponse> reportHistoryFindResponseVOToReportHistoryFindResponseConverter;

  @GetMapping(path = ApiPath.FIND_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
  public MultipleBaseResponse<ReportHistoryFindResponse> findReportHistory(
      @RequestParam String userId,
      @RequestParam(defaultValue = "0", required = false) int page,
      @RequestParam(defaultValue = "1000", required = false) int size) throws ApplicationException {
    Pageable pageable = PageRequest.of(page, size,
        Sort.by(new Sort.Order(Sort.Direction.DESC, SORT_BY_CREATED_DATE)));
    Page<ReportHistoryFindResponseVO> result =
        reportHistoryService.findAllReportHistory(userId, pageable);
    List<ReportHistoryFindResponse> responses = result.getContent().stream()
        .map(data -> reportHistoryFindResponseVOToReportHistoryFindResponseConverter.convert(data))
        .collect(Collectors.toList());
    return MultipleBaseResponse.<ReportHistoryFindResponse>builder()
        .values(responses)
        .metadata(MultipleBaseResponse.Metadata.builder()
            .size(responses.size())
            .totalPage(result.getTotalPages())
            .totalItems(result.getTotalElements()).build())
        .build();
  }
}
