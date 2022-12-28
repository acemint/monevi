package com.monevi.repository;

import com.monevi.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, String>  {
    Optional<UserAccount> findByNimAndMarkForDeleteIsFalse(String nim);
    Optional<UserAccount> findByEmailAndMarkForDeleteFalse(String email);
}
