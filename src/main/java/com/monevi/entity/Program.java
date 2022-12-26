package com.monevi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = Program.ENTITY_NAME)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Program extends BaseEntity {

  public static final String ENTITY_NAME = "MONEVI_PROGRAM";
  public static final String NAME_COLUMN_NAME = "NAME";
  public static final String BUDGET_COLUMN_NAME = "BUDGET";
  public static final String SUBSIDY_COLUMN_NAME = "SUBSIDY";
  public static final String START_DATE_COLUMN_NAME = "START_DATE";
  public static final String END_DATE_COLUMN_NAME = "END_DATE";

  @Column(name = Program.NAME_COLUMN_NAME, nullable = false)
  private String name;

  @Column(name = Program.BUDGET_COLUMN_NAME, nullable = false)
  private double budget;

  @Column(name = Program.SUBSIDY_COLUMN_NAME, nullable = false)
  private double subsidy = 0;

  @Column(name = Program.START_DATE_COLUMN_NAME, nullable = false)
  private Timestamp startDate;

  @Column(name = Program.END_DATE_COLUMN_NAME, nullable = false)
  private Timestamp endDate;

  @Builder.Default
  @OneToMany(cascade = CascadeType.ALL)
  private Set<Transaction> transactions = new HashSet<>();

}
