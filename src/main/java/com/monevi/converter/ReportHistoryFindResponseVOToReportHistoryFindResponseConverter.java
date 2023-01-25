package com.monevi.converter;

import com.monevi.dto.response.ReportHistoryFindResponse;
import com.monevi.dto.vo.ReportHistoryFindResponseVO;
import com.monevi.util.DateUtils;
import org.springframework.stereotype.Component;

@Component(value = ReportHistoryFindResponseVOToReportHistoryFindResponseConverter.COMPONENT_NAME
    + Converter.SUFFIX_BEAN_NAME)
public class ReportHistoryFindResponseVOToReportHistoryFindResponseConverter
    implements Converter<ReportHistoryFindResponseVO, ReportHistoryFindResponse> {

  public static final String COMPONENT_NAME =
      "reportHistoryFindResponseVOToReportHistoryFindResponse";

  @Override
  public ReportHistoryFindResponse convert(ReportHistoryFindResponseVO source) {
    return ReportHistoryFindResponse.builder()
        .organizationName(source.getOrganizationName())
        .reportPeriod(source.getReportPeriod().getTime())
        .remarks(source.getRemarks())
        .userName(source.getUserName())
        .createdDate(source.getCreatedDate().getTime())
        .build();
  }
}
