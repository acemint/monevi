package com.monevi.enums;

public enum TransactionType {

  NON_DAILY ("NON RUTIN"),
  DAILY ("RUTIN");

  private final String name;

  TransactionType(String name) {
    this.name = name;
  }
}
