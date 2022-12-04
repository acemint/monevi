package com.monevi.enums;

public enum GeneralLedgerAccount {

  CASH ("KAS"),
  BANK ("BANK");

  private final String name;

  GeneralLedgerAccount(String name) {
    this.name = name;
  }

}
