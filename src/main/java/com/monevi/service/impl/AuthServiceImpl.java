package com.monevi.service.impl;

import com.monevi.dto.request.UserLoginRequest;
import com.monevi.dto.response.UserLoginResponse;
import com.monevi.security.jwt.JwtTokenUtils;
import com.monevi.security.service.UserDetailsImpl;
import com.monevi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  JwtTokenUtils jwtTokenUtils;

  @Override
  public UserLoginResponse authenticateUser(UserLoginRequest request) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String accessToken = jwtTokenUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    return toUserLoginResponse(userDetails, accessToken);
  }

  private UserLoginResponse toUserLoginResponse(UserDetailsImpl user, String accessToken) {
    return UserLoginResponse.builder()
        .id(user.getId())
        .username(user.getEmail())
        .email(user.getEmail())
        .role(user.getAuthorities().stream().findFirst().get().getAuthority())
        .organizationRegionId(user.getOrganizationRegionId())
        .regionId(user.getRegionId())
        .type("Bearer")
        .accessToken(accessToken).build();
  }
}
