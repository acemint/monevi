package com.monevi.controller;


import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.monevi.dto.request.ResetPasswordRequest;
import com.monevi.dto.request.UserLoginRequest;
import com.monevi.dto.response.BaseResponse;
import com.monevi.dto.response.UserLoginResponse;
import com.monevi.exception.ApplicationException;
import com.monevi.service.AuthService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(ApiPath.BASE + ApiPath.AUTH)
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping(value = ApiPath.LOGIN, consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<UserLoginResponse> authenticateUser(
      @Valid @RequestBody UserLoginRequest loginRequest) {
    UserLoginResponse response = this.authService.authenticateUser(loginRequest);
    return BaseResponse.<UserLoginResponse>builder()
       .value(response).build();
  }
  
  @GetMapping(value = ApiPath.RESET_PASSWORD_REQUEST, produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<Boolean> generateResetPasswordRequest(@RequestParam @NotBlank String email)
      throws ApplicationException {
    Boolean response = this.authService.generateResetPasswordToken(email);
    return BaseResponse.<Boolean>builder().value(response).build();
  }

  @PostMapping(value = ApiPath.RESET_PASSWORD, produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<Boolean> resetPassword(@RequestParam @NotBlank String token,
      @Valid @RequestBody ResetPasswordRequest request) throws ApplicationException {
    Boolean response = this.authService.resetPassword(token, request);
    return BaseResponse.<Boolean>builder().value(response).build();
  }
}
