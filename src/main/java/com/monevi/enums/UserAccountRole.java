package com.monevi.enums;

public enum UserAccountRole {

  TREASURER ("BENDAHARA"),
  CHAIRMAN("KETUA"),
  SUPERVISOR ("SUPERVISOR");

  private final String name;

  UserAccountRole(String name) {
    this.name = name;
  }

}
