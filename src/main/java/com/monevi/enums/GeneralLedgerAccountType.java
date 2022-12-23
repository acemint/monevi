package com.monevi.enums;

public enum GeneralLedgerAccountType {

  CASH ("KAS"),
  BANK ("BANK");

  private final String name;

  GeneralLedgerAccountType(String name) {
    this.name = name;
  }

}
