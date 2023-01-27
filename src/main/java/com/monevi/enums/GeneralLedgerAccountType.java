package com.monevi.enums;

import com.monevi.constant.ErrorMessages;

public enum GeneralLedgerAccountType {

  CASH ("CASH", "KAS"),
  BANK ("BANK", "BANK");

  private final String name;
  private final String excelValue;

  GeneralLedgerAccountType(String name, String excelValue) {
    this.name = name;
    this.excelValue = excelValue;
  }

  public static GeneralLedgerAccountType convertExcelValue(String value) {
    if (BANK.excelValue.equals(value.toUpperCase())) {
      return BANK;
    } else if (CASH.excelValue.equals(value.toUpperCase())) {
      return CASH;
    } else {
      throw new IllegalArgumentException(ErrorMessages.INVALID_GENERAL_LEDGER_ACCOUNT_TYPE);
    }
  }
}
