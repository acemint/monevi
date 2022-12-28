package com.monevi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = Terms.ENTITY_NAME)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Terms extends BaseEntity {

  public static final String ENTITY_NAME = "MONEVI_TERMS";
  public static final String PERIOD_MONTH_COLUMN_NAME = "PERIOD_MONTH";
  public static final String PERIOD_YEAR_COLUMN_NAME = "PERIOD_YEAR";
  public static final String ROLE_COLUMN_NAME = "ROLE";
  public static final String ORGANIZATION_REGION_ID_COLUMN_NAME = "ORGANIZATION_REGION_ID";
  public static final String USER_ACCOUNT_ID_COLUMN_NAME = "USER_ACCOUNT_ID";
  public static final String LOCKED_ACCOUNT_COLUMN_NAME = "LOCKED_ACCOUNT";

  @Column(name = Terms.PERIOD_MONTH_COLUMN_NAME, nullable = false)
  private int periodMonth;

  @Column(name = Terms.PERIOD_YEAR_COLUMN_NAME, nullable = false)
  private int periodYear;

  @Column(name = Terms.ROLE_COLUMN_NAME, nullable = false)
  private String role;

  @Column(name = Terms.LOCKED_ACCOUNT_COLUMN_NAME, nullable = false)
  private boolean lockedAccount;

  @ManyToOne
  @JoinColumn(name = Terms.ORGANIZATION_REGION_ID_COLUMN_NAME, nullable = false)
  private OrganizationRegion organizationRegion;

  @ManyToOne
  @JoinColumn(name = Terms.USER_ACCOUNT_ID_COLUMN_NAME, nullable = false)
  private UserAccount userAccount;

}
