package com.monevi.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monevi.entity.Terms;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentResponse {

    private String nim;
    private String fullName;
    private String email;
}
