package com.monevi.enums;

import com.monevi.constant.ErrorMessages;

public enum EntryPosition {

  DEBIT ("DEBIT", "DEBIT"),
  CREDIT ("CREDIT", "KREDIT");

  private final String name;
  private final String excelValue;

  EntryPosition(String name, String excelValue) {
    this.name = name;
    this.excelValue = excelValue;
  }

  public static EntryPosition convertExcelValue(String request) {
    if (DEBIT.excelValue.equals(request.toUpperCase())) {
      return DEBIT;
    } else if (CREDIT.excelValue.equals(request.toUpperCase())) {
      return CREDIT;
    } else {
      throw new IllegalArgumentException(ErrorMessages.INVALID_ENTRY_POSITION);
    }
  }
}
