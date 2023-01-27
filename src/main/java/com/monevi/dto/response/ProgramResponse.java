package com.monevi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramResponse {

  private String id;
  private String name;
  private double budget;
  private double subsidy;
  private long startDate;
  private long endDate;
  private int periodYear;
  private boolean lockedProgram;

}
