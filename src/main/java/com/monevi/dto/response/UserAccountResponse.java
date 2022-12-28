package com.monevi.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monevi.enums.UserAccountRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAccountResponse implements Serializable {

    private String id;
    private String nim;
    private String fullName;
    private String email;
    private String role;

}
