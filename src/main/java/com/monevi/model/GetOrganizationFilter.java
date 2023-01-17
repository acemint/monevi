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
public class GetOrganizationFilter {

  private String searchTerm;
  private String regionName;
  private Pageable pageable;

}
