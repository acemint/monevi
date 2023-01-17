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
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = Region.ENTITY_NAME)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Region extends BaseEntity {

  public static final String ENTITY_NAME = "MONEVI_REGION";
  public static final String NAME_COLUMN_NAME = "NAME";
  public static final String ORGANIZATION_REGIONS_MAPPED_BY_COLUMN_NAME = "region";

  @Column(name = Region.NAME_COLUMN_NAME, nullable = false)
  private String name;

  @Builder.Default
  @OneToMany(cascade = CascadeType.ALL, mappedBy = Region.ORGANIZATION_REGIONS_MAPPED_BY_COLUMN_NAME)
  private Set<OrganizationRegion> organizationRegions = new HashSet<>();

}
