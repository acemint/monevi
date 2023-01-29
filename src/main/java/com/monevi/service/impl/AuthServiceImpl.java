package com.monevi.service.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.monevi.configuration.PasswordEncoderConfiguration;
import com.monevi.dto.request.ResetPasswordRequest;
import com.monevi.enums.UserAccountRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.monevi.constant.ErrorMessages;
import com.monevi.dto.request.SendEmailRequest;
import com.monevi.dto.request.UserLoginRequest;
import com.monevi.dto.response.UserLoginResponse;
import com.monevi.entity.UserAccount;
import com.monevi.enums.MessageTemplate;
import com.monevi.exception.ApplicationException;
import com.monevi.repository.UserAccountRepository;
import com.monevi.security.jwt.JwtTokenUtils;
import com.monevi.security.service.UserDetailsImpl;
import com.monevi.service.AuthService;
import com.monevi.service.MessageService;

@Service
public class AuthServiceImpl implements AuthService {

  private static final Long EXPIRY_TIME = 3600000L;
  private static final String URL_KEY = "url";
  private static final String USER_NAME = "name";
  
  @Value("${monevi.redirect.reset-password.url}")
  private String resetPasswordBaseUrl;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtTokenUtils jwtTokenUtils;
  
  @Autowired
  private PasswordEncoderConfiguration passwordEncoder;

  @Autowired
  private MessageService messageService;
  
  @Autowired
  private UserAccountRepository userAccountRepository;

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
        .fullname(user.getFullname())
        .username(user.getEmail())
        .email(user.getEmail())
        .role(user.getAuthorities().stream().findFirst().get().getAuthority())
        .organizationRegionId(user.getOrganizationRegionId())
        .regionId(user.getRegionId())
        .type("Bearer")
        .accessToken(accessToken).build();
  }

  @Override
  public Boolean generateResetPasswordToken(String email, MessageTemplate template)
      throws ApplicationException {
    UserAccount user = this.userAccountRepository.findByEmailAndMarkForDeleteFalse(email)
        .orElseThrow(() -> new ApplicationException(HttpStatus.BAD_REQUEST,
            ErrorMessages.USER_ACCOUNT_NOT_FOUND));

    user.setResetPasswordToken(this.generateUUID());
    user.setResetPasswordTokenExpiredDate(this.generateExpiryTime());
    this.userAccountRepository.save(user);

    this.messageService.sendEmail(this.generateEmailRequest(user, template));
    return Boolean.TRUE;
  }

  private String generateUUID() {
    return UUID.randomUUID().toString();
  }

  private Timestamp generateExpiryTime() {
    Long expiredDate = System.currentTimeMillis() + EXPIRY_TIME;
    return new Timestamp(expiredDate);
  }

  private SendEmailRequest generateEmailRequest(UserAccount userAccount, MessageTemplate template) {
    String honorfics = UserAccountRole.SUPERVISOR.equals(userAccount.getRole()) ? "Bapak/Ibu " : "";
    String name = StringUtils.split(userAccount.getFullName(), " ")[0];
    Map<String, String> variables = new HashMap<>();
    variables.put(USER_NAME, honorfics + name);
    variables.put(URL_KEY, this.generateResetPasswordUrl(userAccount.getResetPasswordToken()));
    return SendEmailRequest.builder()
        .messageTemplateId(template)
        .recipient(userAccount.getEmail())
        .variables(variables).build();
  }

  private String generateResetPasswordUrl(String token) {
    return String.format(resetPasswordBaseUrl, token);
  }

  @Override
  public Boolean resetPassword(String token, ResetPasswordRequest request)
      throws ApplicationException {
    UserAccount user =
        this.userAccountRepository.findByResetPasswordTokenAndMarkForDeleteFalse(token)
            .orElseThrow(() -> new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorMessages.INVALID_TOKEN));

    if (user.getResetPasswordTokenExpiredDate().getTime() <= System.currentTimeMillis()) {
      user.setResetPasswordToken(null);
      user.setResetPasswordTokenExpiredDate(null);
      this.userAccountRepository.save(user);

      throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.INVALID_TOKEN);
    }

    if (StringUtils.equals(request.getNewPassword(), request.getConfirmationPassword())) {
      user.setPassword(this.passwordEncoder.encoder().encode(request.getNewPassword()));
      user.setResetPasswordToken(null);
      user.setResetPasswordTokenExpiredDate(null);
      this.userAccountRepository.save(user);
    } else {
      throw new ApplicationException(HttpStatus.BAD_REQUEST,
          ErrorMessages.WRONG_CONFIRMATION_PASSWORD);
    }
    return Boolean.TRUE;
  }
}
