package com.monevi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.monevi.enums.GeneralLedgerAccountType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = Wallet.ENTITY_NAME)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Wallet extends BaseEntity {

  public static final String ENTITY_NAME = "MONEVI_WALLET";
  public static final String TOTAL_COLUMN_NAME = "TOTAL";
  public static final String NAME_COLUMN_NAME = "NAME";
  public static final String ORGANIZATION_REGION_ID_COLUMN_NAME = "ORGANIZATION_REGION_ID";

  @ManyToOne
  @JoinColumn(name = Wallet.ORGANIZATION_REGION_ID_COLUMN_NAME)
  private OrganizationRegion organizationRegion;

  @Enumerated(value = EnumType.STRING)
  @Column(name = Wallet.NAME_COLUMN_NAME, nullable = false)
  private GeneralLedgerAccountType name;

  @Column(name = Wallet.TOTAL_COLUMN_NAME)
  private double total = 0;
}
