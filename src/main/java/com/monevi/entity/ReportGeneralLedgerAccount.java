package com.monevi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = ReportGeneralLedgerAccount.ENTITY_NAME)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportGeneralLedgerAccount extends BaseEntity {

  public static final String ENTITY_NAME = "MONEVI_REPORT_GENERAL_LEDGER_ACCOUNT";
  public static final String REPORT_ID_COLUMN_NAME = "REPORT_ID";
  public static final String TOTAL_COLUMN_NAME = "TOTAL";
  public static final String NAME_COLUMN_NAME = "NAME";

  @Column(name = ReportGeneralLedgerAccount.NAME_COLUMN_NAME, nullable = false)
  private String name;

  @Column(name = ReportGeneralLedgerAccount.TOTAL_COLUMN_NAME)
  private double total;

  @ManyToOne
  @JoinColumn(name = ReportGeneralLedgerAccount.REPORT_ID_COLUMN_NAME)
  private Report report;
}
