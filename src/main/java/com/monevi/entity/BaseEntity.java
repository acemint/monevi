package com.monevi.entity;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTimeZone;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

  public static final String ID_COLUMN_NAME = "ID";
  public static final String MARK_FOR_DELETE_COLUMN_NAME = "MARK_FOR_DELETE";
  public static final String CREATED_BY_COLUMN_NAME = "CREATED_BY";
  public static final String CREATED_DATE_COLUMN_NAME = "CREATED_DATE";
  public static final String UPDATED_BY_COLUMN_NAME = "UPDATED_BY";
  public static final String UPDATED_DATE_COLUMN_NAME = "UPDATED_DATE";

  @Id
  @Column(name = BaseEntity.ID_COLUMN_NAME)
  private String id = UUID.randomUUID().toString();

  @Column(name = BaseEntity.MARK_FOR_DELETE_COLUMN_NAME, nullable = false)
  private Boolean markForDelete = false;

  @Column(name = BaseEntity.CREATED_BY_COLUMN_NAME, nullable = false)
  private String createdBy;

  @Column(name = BaseEntity.CREATED_DATE_COLUMN_NAME, nullable = false)
  private DateTimeZone createdDate;

  @Column(name = BaseEntity.UPDATED_BY_COLUMN_NAME, nullable = false)
  private String updatedBy;

  @Column(name = BaseEntity.UPDATED_DATE_COLUMN_NAME, nullable = false)
  private DateTimeZone updatedDate;

}
