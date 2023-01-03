package com.monevi.controller;

import com.monevi.dto.response.BaseResponse;
import com.monevi.dto.response.UserAccountResponse;
import com.monevi.exception.ApplicationException;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPath.BASE + "/example")
public class ExampleController {
  
  /*
   * TODO: DELETE EXAMPLE CONTROLLER WHEN NOT NEEDED ANYMORE
   */

  @PreAuthorize("hasRole('SUPERVISOR')")
  @GetMapping(path = ApiPath.SUPERVISOR, produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<Object> testForSupervisorOnly() throws ApplicationException {
    UserAccountResponse response = new UserAccountResponse();
    response.setFullName("Ini Khusus Supervisor");
    response.setEmail("ini.khusus.supervisor@mail.com");

    return BaseResponse.builder().value(response).build();
  }

  @PreAuthorize("hasAnyRole('TREASURER', 'CHAIRMAN')")
  @GetMapping(path = ApiPath.STUDENT, produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<Object> testForStudentOnly() throws ApplicationException {
    UserAccountResponse response = new UserAccountResponse();
    response.setFullName("Ini Khusus Student");
    response.setEmail("ini.khusus.student@mail.com");

    return BaseResponse.builder().value(response).build();
  }
}
