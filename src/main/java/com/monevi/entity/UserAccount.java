package com.monevi.entity;

import com.monevi.enums.UserAccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = UserAccount.ENTITY_NAME)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount extends BaseEntity {

  public static final String ENTITY_NAME = "MONEVI_USER_ACCOUNT";
  public static final String NIM_COLUMN_NAME = "NIM";
  public static final String FULL_NAME_COLUMN_NAME = "FULL_NAME";
  public static final String EMAIL_COLUMN_NAME = "EMAIL";
  public static final String PASSWORD_COLUMN_NAME = "PASSWORD";
  public static final String ROLE_COLUMN_NAME = "ROLE";
  public static final String TERMS_MAPPED_BY_FIELD_NAME = "userAccount";

  @Column(name = UserAccount.NIM_COLUMN_NAME, nullable = false, unique = true)
  private String nim;

  @Column(name = UserAccount.FULL_NAME_COLUMN_NAME, nullable = false)
  private String fullName;

  @Column(name = UserAccount.EMAIL_COLUMN_NAME, nullable = false, unique = true)
  private String email;

  @Column(name = UserAccount.PASSWORD_COLUMN_NAME, nullable = false)
  private String password;

  @Enumerated(value = EnumType.STRING)
  @Column(name = UserAccount.ROLE_COLUMN_NAME, nullable = false)
  private UserAccountType type;

  @Builder.Default
  @OneToMany(cascade = CascadeType.ALL, mappedBy = UserAccount.TERMS_MAPPED_BY_FIELD_NAME)
  private Set<Terms> terms = new HashSet<>();

}
