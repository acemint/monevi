package com.monevi.constant;

public interface ErrorMessages {

  String ORGANIZATION_HAS_EXISTED = "Organisasi sudah terdaftar";
  String ORGANIZATION_DOES_NOT_EXIST = "Organisasi tidak terdaftar";
  String REGION_DOES_NOT_EXIST = "Region tidak terdaftar";
  String REGION_FOR_THIS_ORGANIZATION_HAS_EXISTED = "Organisasi sudah terdaftar di region ini";

  String SORT_BY_IS_NOT_SUPPORTED = "Permintaan pengurutan berdasarkan field ini tidak didukung";

  String NIM_ALREADY_EXISTS = "NIM sudah terdaftar";
  String ROLE_ALREADY_TAKEN = "Role %s di organisasi ini sudah terdaftar dan terverifikasi";
  String EMAIL_ALREADY_REGISTERED = "Email sudah terdaftar";
  String ORGANIZATION_REGION_DOES_NOT_EXISTS = "Organisasi tidak terdaftar di region ini";
  String REPORT_DOES_NOT_EXIST = "Laporan keuangan tidak tercatat";
  String USER_ACCOUNT_DOES_NOT_EXIST = "Akun tidak terdaftar";
  String REPORT_HANDLING_IS_PROHIBITED = "Tidak bisa merubah laporan keuangan";
  String FAIL_TO_PROCESS_DATE_FORMAT = "Format tanggal gagal diproses";
  String TRANSACTION_CANNOT_BE_CREATED_BECAUSE_REPORT_HAS_BEEN_APPROVED =
      "Laporan keuangan telah disetujui sehingga tidak bisa diubah";
  String TRANSACTION_NOT_FOUND = "Transaksi tidak ditemukan";
  String ENTRY_POSITION_UNDEFINED = "Entry position transaksi tidak didukung";
  String PROGRAM_NOT_FOUND = "Program tidak ditemukan";

  String USER_ACCOUNT_NOT_FOUND = "Akun tidak ditemukan";
  String INVALID_STUDENT_ROLE = "Role ini bukan merupakan role milik mahasiswa";
  String INVALID_ROLE = "Role tidak valid";
  String INVALID_MONTH = "Bulan tidak valid";
  String INVALID_TOKEN = "Token tidak valid";
  String WRONG_CONFIRMATION_PASSWORD = "Password konfirmasi tidak sesuai";

  String INVALID_FILE_TYPE = "Tipe file harus berupa file Excel (.xlsx)";
  String NO_DATA_IN_EXCEL = "Tidak ada data dalam Excel";
  String INVALID_ENTRY_POSITION = "Entry position tidak valid";
  String INVALID_GENERAL_LEDGER_ACCOUNT_TYPE = "Tipe general ledger account tidak valid";
  String INVALID_TRANSACTION_TYPE = "Tipe traksaksi tidak valid";
  String FILE_TOO_LARGE = "Ukuran file terlalu besar";
  String AMOUNT_MUST_BE_GREATER_THAN_ZERO = "Amount harus lebih besar dari nol";
  String FAIL_TO_CREATE_TRANSACTION = "Pembuatan transaksi gagal";
  String START_DATE_MUST_NOT_BE_BLANK = "Tanggal mulai tidak boleh kosong";
  String END_DATE_MUST_NOT_BE_BLANK = "Tanggal selesai tidak boleh kosong";
  String USER_AND_ORGANIZATION_REGION_NOT_MATCH =
      "Data organisasi user tidak sesuai dengan data organisasi yang dimasukkan";
  String TOTAL_AND_OPNAME_NOT_MATCH = "Total %s dan data opname tidak sesuai";
  String PROGRAM_IS_LOCKED = "Program sudah dikunci dan tidak bisa diubah";
  String PROGRAM_HANDLING_IS_PROHIBITED = "Tidak bisa menambahkan program";
  String INVALID_START_DATE_END_DATE = "Tanggal selesai tidak boleh mendahului tanggal mulai";
  String INVALID_PROGRAM_DATE = "Tanggal tidak boleh kurang dari hari ini";
  String INVALID_TRANSACTION_DATE = "Tanggal tidak boleh lebih dari hari ini";
  String FAILED_TO_SEND_EMAIL = "Gagal mengirim email";
  String MUST_NOT_BE_BLANK = "Data tidak boleh kosong";
  String MUST_BE_POSITIVE = "Nominal transaksi tidak boleh kurang dari sama dengan 0";
  String MUST_BE_POSITIVE_OR_ZERO = "Nominal transaksi tidak boleh kurang dari 0";
}
