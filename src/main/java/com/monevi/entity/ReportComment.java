package com.monevi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = ReportComment.ENTITY_NAME)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportComment extends BaseEntity {

  public static final String ENTITY_NAME = "MONEVI_REPORT_COMMENT";
  public static final String CONTENT_COLUMN_NAME = "CONTENT";
  public static final String COMMENTED_BY_COLUMN_NAME = "COMMENTED_BY";
  public static final String REPORT_ID_COLUMN_NAME = "REPORT_ID";

  @Column(name = ReportComment.CONTENT_COLUMN_NAME)
  private String content;

  @Column(name = ReportComment.COMMENTED_BY_COLUMN_NAME)
  private String commentedBy;
  
  @OneToOne
  @JoinColumn(name = ReportComment.REPORT_ID_COLUMN_NAME, nullable = false)
  private Report report;


}
