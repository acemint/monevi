package com.monevi.repository;

import com.monevi.entity.UserAccount;
import com.monevi.enums.UserAccountRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
  Optional<UserAccount> findByNimAndMarkForDeleteIsFalse(String nim);

  Optional<UserAccount> findByPeriodMonthAndPeriodYearAndRoleAndLockedAccountFalseAndMarkForDeleteFalse(
      Integer periodMonth, Integer periodYear, UserAccountRole role);

  Optional<UserAccount> findByEmailAndMarkForDeleteFalse(String email);

  Optional<UserAccount> findByIdAndMarkForDeleteIsFalse(String id);
}
