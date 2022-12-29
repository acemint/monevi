package com.monevi.controller;

import com.monevi.converter.Converter;
import com.monevi.converter.UserAccountToUserAccountResponseConverter;
import com.monevi.dto.request.CreateStudentRequest;
import com.monevi.dto.request.CreateSupervisorRequest;
import com.monevi.dto.response.BaseResponse;
import com.monevi.dto.response.UserAccountResponse;
import com.monevi.entity.UserAccount;
import com.monevi.exception.ApplicationException;
import com.monevi.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(ApiPath.BASE + ApiPath.STUDENT)
public class UserAccountController {

  @Autowired
  private UserAccountService userAccountService;

  @Autowired
  @Qualifier(UserAccountToUserAccountResponseConverter.COMPONENT_NAME + Converter.SUFFIX_BEAN_NAME)
  private Converter<UserAccount, UserAccountResponse> userAccountToUserAccountResponse;

  @PostMapping(value = ApiPath.REGISTER + ApiPath.STUDENT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<UserAccountResponse> createStudent(
      @Valid @RequestBody CreateStudentRequest request)
      throws ApplicationException {
    UserAccount userAccount = this.userAccountService.register(request);
    return BaseResponse.<UserAccountResponse>builder()
             .value(this.userAccountToUserAccountResponse.convert(userAccount))
             .build();
  }

  @PostMapping(value = ApiPath.REGISTER + ApiPath.SUPERVISOR, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<UserAccountResponse> createSupervisor(
      @Valid @RequestBody CreateSupervisorRequest request)
      throws ApplicationException {
    UserAccount userAccount = this.userAccountService.register(request);
    return BaseResponse.<UserAccountResponse>builder()
        .value(this.userAccountToUserAccountResponse.convert(userAccount))
        .build();
  }

}
