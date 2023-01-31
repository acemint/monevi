package com.monevi.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monevi.constant.ErrorMessages;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationCreateNewRequest implements Serializable {

  @NotNull(message = ErrorMessages.MUST_NOT_BE_BLANK)
  private String name;

  @NotNull(message = ErrorMessages.MUST_NOT_BE_BLANK)
  private String abbreviation;

  private Set<String> regionNames;

}
