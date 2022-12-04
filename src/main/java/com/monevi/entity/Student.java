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
@Table(name = Student.ENTITY_NAME)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student extends BaseEntity {

  public static final String ENTITY_NAME = "MONEVI_STUDENT";
  public static final String NIM_COLUMN_NAME = "NIM";
  public static final String FULL_NAME_COLUMN_NAME = "FULL_NAME";
  public static final String EMAIL_COLUMN_NAME = "EMAIL";
  public static final String PASSWORD_COLUMN_NAME = "PASSWORD";

  @Column(name = Student.NIM_COLUMN_NAME, nullable = false, unique = true)
  private String nim;

  @Column(name = Student.FULL_NAME_COLUMN_NAME, nullable = false)
  private String fullName;

  @Column(name = Student.EMAIL_COLUMN_NAME, nullable = false, unique = true)
  private String email;

  @Column(name = Student.PASSWORD_COLUMN_NAME, nullable = false)
  private String password;

  @Builder.Default
  @OneToMany(cascade = CascadeType.ALL)
  private Set<Terms> terms = new HashSet<>();

}
