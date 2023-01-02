package com.monevi.service;

import com.monevi.dto.request.UserLoginRequest;
import com.monevi.dto.response.UserLoginResponse;

public interface AuthService {

  UserLoginResponse authenticateUser(UserLoginRequest request);
}
