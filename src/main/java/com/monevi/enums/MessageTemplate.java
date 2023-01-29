package com.monevi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageTemplate {

  ACCOUNT_VERIFICATION("Verifikasi Akun Anda", "supervisor-account-verification-template.html"),
  RESET_PASSWORD("Permintaan Pengaturan Ulang Password", "reset-password-template.html"),
  APPROVED_ACCOUNT("Akun Anda Telah Diverifikasi", "approved-account-template.html"),
  SUBMITTED_REPORT("Permintaan Review: Laporan Keuangan %s Bulan %s %s", "submitted-report-template.html"),
  APPROVED_REPORT( "Laporan Keuangan Bulan %s %s Telah Disetujui", "approved-report-template.html"),
  DECLINED_REPORT("Laporan Keuangan Bulan %s %s Telah Diperiksa", "declined-report-template.html");

  private final String subject;
  private final String templateFile;
}
