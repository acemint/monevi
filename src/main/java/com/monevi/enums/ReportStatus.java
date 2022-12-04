package com.monevi.enums;

public enum ReportStatus {

  UNAPPROVED ("UNAPPROVED"),
  APPROVED_BY_CHAIRMAN ("APPROVED BY CHAIRMAN"),
  APPROVED_BY_SUPERVISOR ("APPROVED BY SUPERVISOR");

  private final String name;

  ReportStatus(String name) {
    this.name = name;
  }

}
