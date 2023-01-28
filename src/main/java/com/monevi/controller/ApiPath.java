package com.monevi.controller;


public class ApiPath {

  public static final String BASE = "/api/monevi";

  public static final String ORGANIZATION = "/organization";
  public static final String ORGANIZATION_REGION = "/organization-region";
  public static final String REGION = "/region";
  public static final String STUDENT = "/student";
  public static final String SUPERVISOR = "/supervisor";
  public static final String TRANSACTION = "/transaction";
  public static final String REPORT = "/report";
  public static final String PROGRAM = "/program";
  public static final String USER = "/user";
  public static final String AUTH = "/auth";
  public static final String HISTORY = "/history";
  public static final String WALLET = "/wallet";

  public static final String FIND_ALL = "/all";
  public static final String CREATE_NEW = "/create-new";
  public static final String ADD_REGION = "/add-region";
  public static final String REJECT = "/reject";
  public static final String APPROVE = "/approve";
  public static final String LOCK = "/lock";
  public static final String REGISTER = "/register";
  public static final String LOGIN = "/login";
  public static final String RESET_PASSWORD = "/reset-password";
  public static final String RESET_PASSWORD_REQUEST = "/request" + RESET_PASSWORD;
  public static final String SUBMIT = "/submit";
  public static final String APPROVE_ACCOUNT = "/approve-account";
  public static final String DECLINE_ACCOUNT = "/decline-account";
  public static final String FIND_ALL_STUDENT = "/find-all-student";
  public static final String SEND_EMAIL = "/send-email";
  public static final String EDIT = "/edit";
  public static final String DELETE = "/delete";
  public static final String SUMMARIZE = "/summarize";
  public static final String CONVERT_EXCEL = "/convert-excel";
  public static final String FIND_ORGANIZATION_WITH_PROGRAM = FIND_ALL + "/program-exists";
  public static final String FIND_ORGANIZATION_WITH_REPORT = FIND_ALL + "/report-exists";
  public static final String CALCULATE = "/calculate";
}
