package com.monevi.entity;

import com.monevi.enums.UserAccountRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

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
  public static final String PERIOD_MONTH_COLUMN_NAME = "PERIOD_MONTH";
  public static final String PERIOD_YEAR_COLUMN_NAME = "PERIOD_YEAR";
  public static final String LOCKED_ACCOUNT_COLUMN_NAME = "LOCKED_ACCOUNT";
  public static final String ORGANIZATION_REGION_ID_COLUMN_NAME = "ORGANIZATION_REGION_ID";
  public static final String REGION_ID_COLUMN_NAME = "REGION_ID";
  public static final String RESET_PASSWORD_TOKEN_COLUMN_NAME = "RESET_PASSWORD_TOKEN";
  public static final String RESET_PASSWORD_TOKEN_EXPIRED_DATE_COLUMN_NAME = "RESET_PASSWORD_TOKEN_EXPIRED_DATE";
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
  private UserAccountRole role;

  @Column(name = UserAccount.PERIOD_MONTH_COLUMN_NAME)
  private Integer periodMonth;

  @Column(name = UserAccount.PERIOD_YEAR_COLUMN_NAME)
  private Integer periodYear;

  @Builder.Default
  @Column(name = UserAccount.LOCKED_ACCOUNT_COLUMN_NAME, nullable = false)
  private Boolean lockedAccount = true;

  @ManyToOne
  @JoinColumn(name = UserAccount.ORGANIZATION_REGION_ID_COLUMN_NAME)
  private OrganizationRegion organizationRegion;

  @ManyToOne
  @JoinColumn(name = UserAccount.REGION_ID_COLUMN_NAME)
  private Region region;

  @Column(name = UserAccount.RESET_PASSWORD_TOKEN_COLUMN_NAME, unique = true)
  private String resetPasswordToken;

  @Column(name = UserAccount.RESET_PASSWORD_TOKEN_EXPIRED_DATE_COLUMN_NAME)
  private Timestamp resetPasswordTokenExpiredDate;
}
