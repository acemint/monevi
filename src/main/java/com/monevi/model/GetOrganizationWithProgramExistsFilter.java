package com.monevi.model;

import org.springframework.data.domain.Pageable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetOrganizationWithProgramExistsFilter {

  private String regionId;
  private Integer periodYear;
  private Pageable pageable;
}
