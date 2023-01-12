package com.monevi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monevi.entity.UserAccount;

public interface UserAccountRepository
    extends JpaRepository<UserAccount, String>, UserAccountCustomRepository {
  Optional<UserAccount> findByNimAndMarkForDeleteIsFalse(String nim);

  Optional<UserAccount> findByEmailAndMarkForDeleteFalse(String email);

  Optional<UserAccount> findByIdAndMarkForDeleteIsFalse(String id);
  
  Optional<UserAccount> findByResetPasswordTokenAndMarkForDeleteFalse(String resetPasswordToken);
}
