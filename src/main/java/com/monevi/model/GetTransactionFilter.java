package com.monevi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetTransactionFilter {

  private String organizationRegionId;
  private String startDate;
  private String endDate;
  private Pageable pageable;

}
