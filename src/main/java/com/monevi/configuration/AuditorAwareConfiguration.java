package com.monevi.configuration;

import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.monevi.security.service.UserDetailsImpl;

public class AuditorAwareConfiguration implements AuditorAware<String> {

  private static final String DEFAULT_AUDITOR = "MONEVI";

  @Override
  public Optional<String> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (Objects.isNull(authentication) || !authentication.isAuthenticated()
        || this.isValidAuthenticationPrincipalClass(authentication)) {
      return Optional.of(DEFAULT_AUDITOR);
    }
    UserDetailsImpl auditor = ((UserDetailsImpl) authentication.getPrincipal());
    return Optional.of(auditor.getEmail());
  }

  private Boolean isValidAuthenticationPrincipalClass(Authentication authentication) {
    return !(UserDetailsImpl.class).equals(authentication.getPrincipal().getClass());
  }
}
