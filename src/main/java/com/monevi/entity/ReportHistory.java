package com.monevi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.monevi.enums.ReportStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = ReportHistory.ENTITY_NAME)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportHistory extends BaseEntity {

  public static final String ENTITY_NAME = "MONEVI_REPORT_HISTORY";
  public static final String USER_ID_COLUMN_NAME = "USER_ID";
  public static final String REPORT_ID_COLUMN_NAME = "REPORT_ID";
  public static final String REMARKS_COLUMN_NAME = "REMARKS";

  @OneToOne
  @JoinColumn(name = ReportHistory.USER_ID_COLUMN_NAME, nullable = false)
  private UserAccount user;

  @ManyToOne
  @JoinColumn(name = ReportHistory.REPORT_ID_COLUMN_NAME, nullable = false)
  private Report report;

  @Enumerated(value = EnumType.STRING)
  @Column(name = ReportHistory.REMARKS_COLUMN_NAME, nullable = false)
  private ReportStatus remarks;
}
