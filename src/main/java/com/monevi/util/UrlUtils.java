package com.monevi.util;

import org.springframework.data.domain.Sort;


public abstract class UrlUtils {

  public static Sort.Direction getSortDirection(String isAscending) {
    return isAscending.equalsIgnoreCase("true") ? Sort.Direction.ASC : Sort.Direction.DESC;
  }
}
