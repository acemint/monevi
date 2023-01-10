package com.monevi.model;

import com.monevi.enums.UserAccountRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetStudentFilter {

  private String studentName;
  private Integer periodMonth;
  private Integer periodYear;
  private String organizationName;
  private String regionId;
  private UserAccountRole studentRole;
  private Boolean lockedAccount;
  private Pageable pageable;
}
