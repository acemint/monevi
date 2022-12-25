package com.monevi.entity;

import com.monevi.enums.EntryPosition;
import com.monevi.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTimeZone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = Transaction.ENTITY_NAME)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction extends BaseEntity {

  public static final String ENTITY_NAME = "MONEVI_TRANSACTION";
  public static final String NAME_COLUMN_NAME = "NAME";
  public static final String TRANSACTION_DATE_COLUMN_NAME = "TRANSACTION_DATE";
  public static final String AMOUNT_COLUMN_NAME = "AMOUNT";
  public static final String ENTRY_POSITION_COLUMN_NAME = "ENTRY_POSITION";
  public static final String TYPE_COLUMN_NAME = "TYPE";
  public static final String DESCRIPTION_COLUMN_NAME = "DESCRIPTION";
  public static final String PROOF_COLUMN_NAME = "PROOF";
  public static final String GENERAL_LEDGER_ACCOUNT_ID_COLUMN_NAME = "GENERAL_LEDGER_ACCOUNT_ID";
  public static final String REPORT_ID_COLUMN_NAME = "REPORT_ID";
  public static final String PROGRAM_ID_COLUMN_NAME = "PROGRAM_ID";

  @Column(name = Transaction.NAME_COLUMN_NAME, nullable = false)
  private String name;

  @Column(name = Transaction.TRANSACTION_DATE_COLUMN_NAME, nullable = false)
  private Timestamp transactionDate;

  @Builder.Default
  @Column(name = Transaction.AMOUNT_COLUMN_NAME, nullable = false)
  private double amount = 0;

  @Enumerated(value = EnumType.STRING)
  @Column(name = Transaction.ENTRY_POSITION_COLUMN_NAME, nullable = false)
  private EntryPosition entryPosition;

  @Builder.Default
  @Enumerated(value = EnumType.STRING)
  @Column(name = Transaction.TYPE_COLUMN_NAME, nullable = false)
  private TransactionType type = TransactionType.NON_DAILY;

  @Column(name = Transaction.DESCRIPTION_COLUMN_NAME)
  private String description;

  @Column(name = Transaction.PROOF_COLUMN_NAME)
  private byte[] proof;

  @ManyToOne
  @JoinColumn(name = Transaction.GENERAL_LEDGER_ACCOUNT_ID_COLUMN_NAME, nullable = false)
  private GeneralLedgerAccount generalLedgerAccount;

  @ManyToOne
  @JoinColumn(name = Transaction.REPORT_ID_COLUMN_NAME, nullable = false)
  private Report report;

  @ManyToOne
  @JoinColumn(name = Transaction.PROGRAM_ID_COLUMN_NAME)
  private Program program;

}
