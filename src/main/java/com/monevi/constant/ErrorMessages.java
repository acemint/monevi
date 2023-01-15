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
  String INVALID_MONTH = "invalid month";
  String INVALID_TOKEN = "invalid token";
  String WRONG_CONFIRMATION_PASSWORD = "wrong confirmation password";
}
