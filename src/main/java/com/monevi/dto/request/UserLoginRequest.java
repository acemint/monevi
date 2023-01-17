package com.monevi.dto.request;

import javax.validation.constraints.NotBlank;

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

  @NotBlank
  @ValidEmail
  private String username;

  @NotBlank
  private String password;
}
