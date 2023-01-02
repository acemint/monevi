package com.monevi.security.service;

import com.monevi.constant.ErrorMessages;
import com.monevi.entity.UserAccount;
import com.monevi.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserAccountRepository userAccountRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserAccount user = userAccountRepository.findByEmailAndMarkForDeleteFalse(username)
        .orElseThrow(() -> new UsernameNotFoundException(ErrorMessages.USER_ACCOUNT_NOT_FOUND));
    return UserDetailsImpl.build(user);
  }
}
