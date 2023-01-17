package com.monevi.util;

import com.monevi.constant.ErrorMessages;
import com.monevi.enums.EntryPosition;
import com.monevi.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class FinanceUtils {

  public static Double getActualAmount(EntryPosition entryPosition, double amount) throws ApplicationException{
    if (entryPosition.equals(EntryPosition.DEBIT)) {
      return amount;
    }
    else if (entryPosition.equals(EntryPosition.CREDIT)){
      return -1 * amount;
    }
    else {
      throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.ENTRY_POSITION_UNDEFINED);
    }
  }

}
