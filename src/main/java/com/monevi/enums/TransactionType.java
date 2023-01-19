package com.monevi.enums;

import com.monevi.constant.ErrorMessages;

public enum TransactionType {

  NON_DAILY ("NON RUTIN"),
  DAILY ("RUTIN");

  private final String name;

  TransactionType(String name) {
    this.name = name;
  }

  public static TransactionType convertExcelValue(String request) {
    if (NON_DAILY.name.equals(request.toUpperCase())) {
      return NON_DAILY;
    } else if (DAILY.name.equals(request.toUpperCase())) {
      return DAILY;
    } else {
      throw new IllegalArgumentException(ErrorMessages.INVALID_TRANSACTION_TYPE);
    }
  }
}
