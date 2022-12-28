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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Timestamp;
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
  public static final String PERIOD_DATE_COLUMN_NAME = "PERIOD_DATE";
  public static final String STATUS_COLUMN_NAME = "STATUS";
  public static final String ORGANIZATION_REGION_ID_COLUMN_NAME = "ORGANIZATION_REGION_ID";
  public static final String GENERAL_LEDGER_ACCOUNT_MAPPED_BY_FIELD_NAME = "report";
  public static final String REPORT_COMMENT_MAPPED_BY_FIELD_NAME = "report";

  @Column(name = Report.PERIOD_DATE_COLUMN_NAME, nullable = false)
  private Timestamp periodDate;

  @Builder.Default
  @Enumerated(value = EnumType.STRING)
  @Column(name = Report.STATUS_COLUMN_NAME, nullable = false)
  private ReportStatus status = ReportStatus.UNAPPROVED;

  @OneToOne(cascade = CascadeType.ALL, mappedBy = Report.REPORT_COMMENT_MAPPED_BY_FIELD_NAME)
  private ReportComment reportComment;

  @Builder.Default
  @OneToMany(cascade = CascadeType.ALL, mappedBy = Report.GENERAL_LEDGER_ACCOUNT_MAPPED_BY_FIELD_NAME)
  private Set<GeneralLedgerAccount> generalLedgerAccounts = new HashSet<>();

  @ManyToOne
  @JoinColumn(name = Report.ORGANIZATION_REGION_ID_COLUMN_NAME, nullable = false)
  private OrganizationRegion organizationRegion;

}
