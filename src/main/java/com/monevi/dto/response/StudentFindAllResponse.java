package com.monevi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentFindAllResponse {

  private String nim;
  private String fullname;
  private String orgName;
  private String orgAbbreviation;
  private String regionName;
  private String role;
  private Integer periodYear;
  private Integer periodMonth;
  private Boolean lockedAccount;
}
