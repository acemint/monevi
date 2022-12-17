package com.monevi.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @NotBlank
    @Length(min = 10, max = 10, message = "nim must consists of 10 characters")
    private String nim;

    @NotBlank
    private String fullName;

    @ValidEmail
    private String email;

    @NotBlank
    private String password;

    @NotNull
    private Integer periodMonth;

    @NotNull
    private Integer periodYear;

    @NotBlank
    private String organizationName;

    @NotBlank
    private String regionName;

    @NotBlank
    private String role;
}
