package com.monevi.service;

import com.monevi.dto.request.ResetPasswordRequest;
import com.monevi.dto.request.UserLoginRequest;
import com.monevi.dto.response.UserLoginResponse;
import com.monevi.exception.ApplicationException;

public interface AuthService {

  UserLoginResponse authenticateUser(UserLoginRequest request);

  Boolean generateResetPasswordToken(String email) throws ApplicationException;

  Boolean resetPassword(String token, ResetPasswordRequest request) throws ApplicationException;
}
