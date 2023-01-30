package com.monevi.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monevi.constant.ErrorMessages;
import com.monevi.validation.annotation.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateSupervisorRequest {

    @NotBlank(message = ErrorMessages.MUST_NOT_BE_BLANK)
    private String fullName;

    @ValidEmail
    private String email;

    @NotBlank(message = ErrorMessages.MUST_NOT_BE_BLANK)
    @Length(min = 8, message = "minimum 8 characters")
    private String password;

    @NotBlank(message = ErrorMessages.MUST_NOT_BE_BLANK)
    private String regionName;
}
