package com.monevi.enums;

public enum UserAccountType {

  STUDENT ("STUDENT"),
  SUPERVISOR ("SUPERVISOR");

  private final String name;

  UserAccountType(String name) {
    this.name = name;
  }

}
