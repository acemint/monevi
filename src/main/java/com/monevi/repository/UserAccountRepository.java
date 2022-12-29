package com.monevi.repository;

import com.monevi.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, String>  {
    Optional<UserAccount> findByNimAndPeriodMonthAndPeriodYearAndMarkForDeleteIsFalse(String nim, Integer periodMonth, Integer periodYear);
    Optional<UserAccount> findByEmailAndPeriodMonthAndPeriodYearAndMarkForDeleteFalse(String email, Integer periodMonth, Integer periodYear);
    Optional<UserAccount> findByEmailAndMarkForDeleteFalse(String email);

    Optional<UserAccount> findByIdAndMarkForDeleteIsFalse(String id);
}
