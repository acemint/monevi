package com.monevi.enums;

public enum ReportStatus {

  NOT_SENT("NOT SENT"),
  UNAPPROVED ("UNAPPROVED"),
  APPROVED_BY_CHAIRMAN ("APPROVED BY CHAIRMAN"),
  APPROVED_BY_SUPERVISOR ("APPROVED BY SUPERVISOR"),
  DECLINED("DECLINED");

  private final String name;

  ReportStatus(String name) {
    this.name = name;
  }

}
