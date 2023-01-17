package com.monevi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {

  @NotBlank
  @Length(min = 8, message = "minimum 8 characters")
  private String newPassword;

  @NotBlank
  private String confirmationPassword;
}
