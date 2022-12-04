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
@Table(name = Organization.ENTITY_NAME)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Organization extends BaseEntity {

  public static final String ENTITY_NAME = "MONEVI_ORGANIZATION";
  public static final String NAME_COLUMN_NAME = "NAME";
  public static final String ABBREVIATION_COLUMN_NAME = "ABBREVIATION";

  @Column(name = Organization.NAME_COLUMN_NAME, nullable = false, unique = true)
  private String name;

  @Column(name = Organization.ABBREVIATION_COLUMN_NAME, nullable = false)
  private String abbreviation;

  @Builder.Default
  @OneToMany(cascade = CascadeType.ALL)
  private Set<Terms> terms = new HashSet<>();

  @Builder.Default
  @OneToMany(cascade = CascadeType.ALL)
  private Set<OrganizationRegion> organizationRegions = new HashSet<>();

}
