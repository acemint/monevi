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
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateStudentRequest {

    // TODO: Create validation for student role (MINOR)

    @NotBlank(message = ErrorMessages.MUST_NOT_BE_BLANK)
    @Length(min = 10, max = 10, message = "NIM harus berjumlah 10 karakter")
    private String nim;

    @NotBlank(message = ErrorMessages.MUST_NOT_BE_BLANK)
    private String fullName;

    @ValidEmail
    private String email;

    @NotBlank(message = ErrorMessages.MUST_NOT_BE_BLANK)
    @Length(min = 8, message = "Password minimal 8 karakter")
    private String password;

    @NotNull(message = ErrorMessages.MUST_NOT_BE_BLANK)
    private Integer periodMonth;

    @NotNull(message = ErrorMessages.MUST_NOT_BE_BLANK)
    private Integer periodYear;

    @NotBlank(message = ErrorMessages.MUST_NOT_BE_BLANK)
    private String organizationName;

    @NotBlank(message = ErrorMessages.MUST_NOT_BE_BLANK)
    private String regionName;

    @NotBlank(message = ErrorMessages.MUST_NOT_BE_BLANK)
    private String role;

}
