package com.monevi.util;

import com.monevi.constant.ErrorMessages;
import com.monevi.entity.Organization_;
import com.monevi.entity.Program_;
import com.monevi.exception.ApplicationException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

public class ProgramUrlUtils extends UrlUtils{

  private static List<String> AVAILABLE_PROGRAM_SORTED_BY = Arrays.asList(
      Program_.NAME);

  public static void checkValidSortedBy(String sortedBy) throws ApplicationException {
    if (!AVAILABLE_PROGRAM_SORTED_BY.contains(sortedBy)) {
      throw new ApplicationException(HttpStatus.BAD_REQUEST, ErrorMessages.SORT_BY_IS_NOT_SUPPORTED);
    }
  }

}
