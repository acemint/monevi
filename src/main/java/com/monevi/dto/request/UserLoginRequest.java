package com.monevi.dto.request;

import javax.validation.constraints.NotBlank;

import com.monevi.constant.ErrorMessages;
import com.monevi.validation.annotation.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest {

  @NotBlank(message = ErrorMessages.MUST_NOT_BE_BLANK)
  @ValidEmail
  private String username;

  @NotBlank(message = ErrorMessages.MUST_NOT_BE_BLANK)
  private String password;
}
