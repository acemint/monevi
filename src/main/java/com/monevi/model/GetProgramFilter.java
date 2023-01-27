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
public class GetProgramFilter {

  private String organizationRegionId;
  private Integer periodYear;
  private Pageable pageable;

}
