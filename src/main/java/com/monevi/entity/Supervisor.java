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
@Table(name = Supervisor.ENTITY_NAME)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Supervisor extends BaseEntity {

  public static final String ENTITY_NAME = "MONEVI_SUPERVISOR";
  public static final String FULL_NAME_COLUMN_NAME = "FULL_NAME";
  public static final String EMAIL_COLUMN_NAME = "EMAIL";
  public static final String PASSWORD_COLUMN_NAME = "PASSWORD";

  @Column(name = Supervisor.FULL_NAME_COLUMN_NAME, nullable = false)
  private String fullName;

  @Column(name = Supervisor.EMAIL_COLUMN_NAME, nullable = false, unique = true)
  private String email;

  @Column(name = Supervisor.PASSWORD_COLUMN_NAME, nullable = false)
  private String password;

}
