package com.monevi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageTemplate {

  RESET_PASSWORD("Reset Your Password", "reset-password-template.html"),
  APPROVED_ACCOUNT("Your Account Has Been Approved", "approved-account-template.html"),
  SUBMITTED_REPORT("%s Has Submitted %s %s Report", "submitted-report.html"),
  APPROVED_REPORT( "%s %s Report Has Been Accepted", "approved-report-template.html"),
  DECILNED_REPORT("%s %s Report Has Been Rejected", "declined-report-template.html");

  private final String subject;
  private final String templateFile;
}
