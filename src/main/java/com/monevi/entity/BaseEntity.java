package com.monevi.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
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

  @CreatedBy
  @Column(name = BaseEntity.CREATED_BY_COLUMN_NAME, nullable = false)
  private String createdBy;

  @CreatedDate
  @Column(name = BaseEntity.CREATED_DATE_COLUMN_NAME, nullable = false)
  private Timestamp createdDate;

  @LastModifiedBy
  @Column(name = BaseEntity.UPDATED_BY_COLUMN_NAME, nullable = false)
  private String updatedBy;

  @LastModifiedDate
  @Column(name = BaseEntity.UPDATED_DATE_COLUMN_NAME, nullable = false)
  private Timestamp updatedDate;

}
