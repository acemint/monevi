package com.monevi.enums;

import lombok.Getter;

@Getter
public enum UserAccountRole {

  TREASURER ("BENDAHARA", "ROLE_TREASURER"),
  CHAIRMAN("KETUA", "ROLE_CHAIRMAN"),
  SUPERVISOR ("SUPERVISOR", "ROLE_SUPERVISOR");

  private final String name;
  private final String authority;

  UserAccountRole(String name, String authority) {
    this.name = name;
    this.authority = authority;
  }

}
