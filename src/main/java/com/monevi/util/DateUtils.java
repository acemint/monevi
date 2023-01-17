package com.monevi.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.http.HttpStatus;

import com.monevi.constant.ErrorMessages;
import com.monevi.exception.ApplicationException;

public class DateUtils {

  public static Timestamp dateInputToTimestamp(String date) throws ApplicationException {
    try {
      DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy");
      return new Timestamp(dateTimeFormatter.parseDateTime(date).getMillis());
    }
    catch (IllegalArgumentException e) {
      throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.FAIL_TO_PROCESS_DATE_FORMAT);
    }
  }

  public static Integer dateInputToMonth(String date) throws ApplicationException {
    try {
      DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy");
      return dateTimeFormatter.parseDateTime(date).getMonthOfYear();
    }
    catch (IllegalArgumentException e) {
      throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.FAIL_TO_PROCESS_DATE_FORMAT);
    }
  }

  public static Integer dateInputToYear(String date) throws ApplicationException {
    try {
      DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy");
      return dateTimeFormatter.parseDateTime(date).getYear();
    }
    catch (IllegalArgumentException e) {
      throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.FAIL_TO_PROCESS_DATE_FORMAT);
    }
  }

  public static String dateToFirstDayOfMonth(String date) throws ApplicationException {
    DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy");
    DateTime dateTime = dateTimeFormatter.parseDateTime(date).dayOfMonth().withMinimumValue();
    return dateTimeFormatter.print(dateTime);
  }

  public static String dateToLastDayOfMonth(String date) throws ApplicationException {
    DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy");
    DateTime dateTime = dateTimeFormatter.parseDateTime(date).dayOfMonth().withMaximumValue();
    return dateTimeFormatter.print(dateTime);
  }

  public static String deductMonthFromDate(String date, int amount) throws ApplicationException {
    DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy");
    DateTime dateTime = dateTimeFormatter.parseDateTime(date);
    dateTime.minusMonths(amount);
    return dateTimeFormatter.print(dateTime);
  }

  public static String convertTimestampToString(Timestamp timestamp) throws ApplicationException {
    Date date = new Date(timestamp.getTime());
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    return dateFormat.format(date);
  }
  
  public static Long convertDateToLong(String date) throws ApplicationException {
    DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy");
    DateTime dateTime = dateTimeFormatter.parseDateTime(date);
    return dateTime.getMillis();
  }

  public static String convertToDefaultPattern(String date) throws ApplicationException {
    String defaultFormat = "%s/%s/%s";
    String[] splittedDate = StringUtils.split(date, '-');
    return String.format(defaultFormat, splittedDate[2], splittedDate[1], splittedDate[0]);
  }
}
