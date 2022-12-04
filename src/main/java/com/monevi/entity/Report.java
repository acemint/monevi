package com.monevi.entity;

import com.monevi.enums.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = Report.ENTITY_NAME)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Report extends BaseEntity {

  public static final String ENTITY_NAME = "MONEVI_REPORT";
  public static final String NAME_COLUMN_NAME = "NAME";
  public static final String PERIOD_MONTH_COLUMN_NAME = "PERIOD_MONTH";
  public static final String PERIOD_YEAR_COLUMN_NAME = "PERIOD_YEAR";
  public static final String STATUS_COLUMN_NAME = "STATUS";

  @Column(name = Report.NAME_COLUMN_NAME, nullable = false)
  private String name;

  @Column(name = Report.PERIOD_MONTH_COLUMN_NAME, nullable = false)
  private int periodMonth;

  @Column(name = Report.PERIOD_YEAR_COLUMN_NAME, nullable = false)
  private int periodYear;

  @Builder.Default
  @Enumerated(value = EnumType.STRING)
  @Column(name = Report.STATUS_COLUMN_NAME, nullable = false)
  private String status = ReportStatus.UNAPPROVED.name();

  @Builder.Default
  @OneToMany(cascade = CascadeType.ALL)
  private Set<Transaction> transactions = new HashSet<>();

}
