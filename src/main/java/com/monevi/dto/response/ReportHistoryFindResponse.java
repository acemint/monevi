package com.monevi.dto.response;

import com.monevi.enums.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportHistoryFindResponse {

    private String organizationName;
    private Long reportPeriod;
    private ReportStatus remarks;
    private String userName;
    private Long createdDate;
}
