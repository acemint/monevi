package com.monevi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = OrganizationRegion.ENTITY_NAME)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationRegion extends BaseEntity {

  public static final String ENTITY_NAME = "MONEVI_ORGANIZATION_REGION";
  public static final String ORGANIZATION_ID_COLUMN_NAME = "ORGANIZATION_ID";
  public static final String REGION_ID_COLUMN_NAME = "REGION_ID";
  public static final String REPORT_MAPPED_BY_FIELD_NAME = "organizationRegion";

  @Builder.Default
  @OneToMany(cascade = CascadeType.ALL, mappedBy = OrganizationRegion.REPORT_MAPPED_BY_FIELD_NAME)
  private Set<Report> reports = new HashSet<>();

  @ManyToOne
  @JoinColumn(name = OrganizationRegion.ORGANIZATION_ID_COLUMN_NAME, nullable = false)
  private Organization organization;

  @ManyToOne
  @JoinColumn(name = OrganizationRegion.REGION_ID_COLUMN_NAME, nullable = false)
  private Region region;

}
