package com.monevi.converter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.monevi.dto.response.ReportResponse;
import com.monevi.entity.Report;
import com.monevi.entity.ReportComment;

@Component(value = ReportToReportResponseConverter.REPORT_TO_REPORT_RESPONSE_BEAN_NAME
    + Converter.SUFFIX_BEAN_NAME)
public class ReportToReportResponseConverter implements Converter<Report, ReportResponse> {

  public static final String REPORT_TO_REPORT_RESPONSE_BEAN_NAME = "ResponseToResponse";

  @Override
  public ReportResponse convert(Report source) {
    ReportComment reportComment = Optional.ofNullable(source.getReportComment())
        .orElse(ReportComment.builder().build());
    ReportResponse response = ReportResponse.builder()
        .id(source.getId())
        .periodDate(source.getPeriodDate().getTime())
        .status(source.getStatus())
        .comment(reportComment.getContent())
        .commentedBy(reportComment.getCommentedBy())
        .build();
    return response;
  }

}
