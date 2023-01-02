package com.monevi.enums;

public enum GeneralLedgerAccountType {

  CASH ("CASH"),
  BANK ("BANK");

  private final String name;

  GeneralLedgerAccountType(String name) {
    this.name = name;
  }

}
