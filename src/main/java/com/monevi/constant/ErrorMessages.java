package com.monevi.constant;

public interface ErrorMessages {

  String ORGANIZATION_HAS_EXISTED = "organization has existed";
  String ORGANIZATION_DOES_NOT_EXIST = "organization does not exist";
  String REGION_DOES_NOT_EXIST = "region does not exist";
  String REGION_FOR_THIS_ORGANIZATION_HAS_EXISTED = "region for this organization has existed";

  String SORT_BY_IS_NOT_SUPPORTED = "sort by is not supported";

  String NIM_ALREADY_EXISTS = "nim already exists";
  String ROLE_ALREADY_TAKEN = "this role already taken";
  String EMAIL_ALREADY_REGISTERED = "email has already exists";
  String ORGANIZATION_REGION_DOES_NOT_EXISTS = "organization does not exist in this region";
  String REPORT_DOES_NOT_EXIST = "report does not exist";
  String USER_ACCOUNT_DOES_NOT_EXIST = "user account does not exist";
  String REPORT_HANDLING_IS_PROHIBITED = "report handling is prohibited";
  String FAIL_TO_PROCESS_DATE_FORMAT = "fail to process date format";
  String TRANSACTION_CANNOT_BE_CREATED_BECAUSE_REPORT_HAS_BEEN_APPROVED = "report cannot be changed because report has been approved";
  String TRANSACTION_NOT_FOUND = "transaction not found";
  String ENTRY_POSITION_UNDEFINED = "entry position of transaction is unsupported";
  String PROGRAM_NOT_FOUND = "program not found";
  String PROGRAM_NOT_STARTED_OR_ALREADY_ENDED = "program has not started / has already ended";

  String USER_ACCOUNT_NOT_FOUND = "user not found";
  String INVALID_STUDENT_ROLE = "this role is not student's role";
  String INVALID_ROLE = "invalid user role";
  String INVALID_MONTH = "invalid month";
  String INVALID_TOKEN = "invalid token";
  String WRONG_CONFIRMATION_PASSWORD = "wrong confirmation password";

  String INVALID_FILE_TYPE = "file type must be excel file (.xlsx)";
  String NO_DATA_IN_EXCEL = "no data in excel";
  String INVALID_ENTRY_POSITION = "invalid entry position";
  String INVALID_GENERAL_LEDGER_ACCOUNT_TYPE = "invalid general ledger account type";
  String INVALID_TRANSACTION_TYPE = "invalid transaction type";
  String FILE_TOO_LARGE = "file size is too large";
  String AMOUNT_MUST_BE_GREATER_THAN_ZERO = "amount must be greated than zero";
  String FAIL_TO_CREATE_TRANSACTION = "fail to create transaction";
  String START_DATE_MUST_NOT_BE_BLANK = "start date must not be blank";
  String END_DATE_MUST_NOT_BE_BLANK = "end date must not be blank";
  String USER_AND_ORGANIZATION_REGION_NOT_MATCH =
      "user's organization region and organization region request not match";
  String TOTAL_AND_OPNAME_NOT_MATCH = "total %s and opname not match";
  String PROGRAM_IS_LOCKED = "program is locked and can not be edited";
  String PROGRAM_HANDLING_IS_PROHIBITED = "program handling is prohibited";
  String INVALID_START_DATE_END_DATE = "invalid start date and end date";
}
