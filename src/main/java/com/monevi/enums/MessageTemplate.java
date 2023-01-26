package com.monevi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageTemplate {

  RESET_PASSWORD("Reset Your Password", "reset-password-template.html"),
  APPROVED_ACCOUNT("Your Account Has Been Approved", "approved-account-template.html"),
  SUBMITTED_REPORT("Review Request: %s's %s %s Report ", "submitted-report-template.html"),
  APPROVED_REPORT( "%s %s Report Has Been Accepted", "approved-report-template.html"),
  DECLINED_REPORT("%s %s Report Has Been Reviewed", "declined-report-template.html");

  private final String subject;
  private final String templateFile;
}
