package com.monevi.dto.request;

import com.monevi.constant.ErrorMessages;
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

  @NotBlank(message = ErrorMessages.MUST_NOT_BE_BLANK)
  @Length(min = 8, message = "minimum 8 characters")
  private String newPassword;

  @NotBlank(message = ErrorMessages.MUST_NOT_BE_BLANK)
  private String confirmationPassword;
}
