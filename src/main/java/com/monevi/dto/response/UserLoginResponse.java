package com.monevi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponse {

  private String id;
  private String fullname;
  private String username;
  private String email;
  private String role;
  private String organizationRegionId;
  private String regionId;
  private String accessToken;
  private String type;
}
