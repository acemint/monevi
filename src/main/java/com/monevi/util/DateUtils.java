package com.monevi.util;

import com.monevi.constant.ErrorMessages;
import com.monevi.exception.ApplicationException;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

public class DateUtils {

  public static Timestamp dateInputToTimestamp(String date) throws ApplicationException{
    try {
      DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy");
      return new Timestamp(dateTimeFormatter.parseDateTime(date).getMillis());
    }
    catch (IllegalArgumentException e) {
      throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.FAIL_TO_PROCESS_DATE_FORMAT);
    }
  }

}
