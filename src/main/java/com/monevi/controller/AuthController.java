package com.monevi.controller;


import com.monevi.dto.request.UserLoginRequest;
import com.monevi.dto.response.BaseResponse;
import com.monevi.dto.response.UserLoginResponse;
import com.monevi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
}
