package com.monevi.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.monevi.entity.UserAccount;
import com.monevi.enums.UserAccountRole;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {
  private static final long serialVersionUID = -5976584328059357539L;

  private String id;

  private String username;

  private String email;

  @JsonIgnore
  private String password;

  private Collection<? extends GrantedAuthority> authorities;

  private String organizationRegionId;

  private boolean lockedAccount;

  public static UserDetailsImpl build(UserAccount user) {
    List<GrantedAuthority> authority =
        Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getAuthority()));

   String organizationRegionId = "";
   if (UserAccountRole.TREASURER.equals(user.getRole())
       || UserAccountRole.CHAIRMAN.equals(user.getRole())) {
     organizationRegionId = user.getOrganizationRegion().getId();
   }

   return UserDetailsImpl.builder()
        .id(user.getId())
        .username(user.getEmail())
        .email(user.getEmail())
        .password(user.getPassword())
        .organizationRegionId(organizationRegionId)
        .authorities(authority)
        .lockedAccount(user.getLockedAccount()).build();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return BooleanUtils.toBoolean(!lockedAccount);
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
