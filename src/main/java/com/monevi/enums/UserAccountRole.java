package com.monevi.enums;

public enum UserAccountRole {

  TREASURER ("BENDAHARA"),
  LEADER ("KETUA"),
  SUPERVISOR ("SUPERVISOR");

  private final String name;

  UserAccountRole(String name) {
    this.name = name;
  }

}
