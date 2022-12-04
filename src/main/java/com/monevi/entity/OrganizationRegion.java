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
@Table(name = OrganizationRegion.ENTITY_NAME)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationRegion extends BaseEntity {

  public static final String ENTITY_NAME = "MONEVI_ORGANIZATION_REGION";
  public static final String NAME_COLUMN_NAME = "NAME";

  @Column(name = OrganizationRegion.NAME_COLUMN_NAME, nullable = false)
  private String name;

  @Builder.Default
  @OneToMany(cascade = CascadeType.ALL)
  private Set<Report> reports = new HashSet<>();

}
