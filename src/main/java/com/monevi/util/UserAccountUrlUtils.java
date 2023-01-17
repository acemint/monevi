package com.monevi.util;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.monevi.constant.ErrorMessages;
import com.monevi.entity.UserAccount_;
import com.monevi.exception.ApplicationException;

public class UserAccountUrlUtils extends UrlUtils {

  private static List<String> AVAILABLE_USER_ACCOUNT_SORTED_BY =
      Arrays.asList(UserAccount_.CREATED_DATE, UserAccount_.PERIOD_YEAR);

  public static void checkValidSortedBy(String sortedBy) throws ApplicationException {
    if (!AVAILABLE_USER_ACCOUNT_SORTED_BY.contains(sortedBy)) {
      throw new ApplicationException(HttpStatus.BAD_REQUEST,
          ErrorMessages.SORT_BY_IS_NOT_SUPPORTED);
    }
  }
}
