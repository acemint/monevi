package com.monevi.repository;

import java.util.List;
import java.util.Optional;

import com.monevi.entity.Region;
import com.monevi.enums.UserAccountRole;
import org.springframework.data.jpa.repository.JpaRepository;

import com.monevi.entity.UserAccount;

public interface UserAccountRepository
    extends JpaRepository<UserAccount, String>, UserAccountCustomRepository {
  Optional<UserAccount> findByNimAndMarkForDeleteIsFalse(String nim);

  Optional<UserAccount> findByEmailAndMarkForDeleteFalse(String email);

  Optional<UserAccount> findByIdAndMarkForDeleteIsFalse(String id);
  
  Optional<UserAccount> findByResetPasswordTokenAndMarkForDeleteFalse(String resetPasswordToken);

  Optional<List<UserAccount>> findAllByRoleAndRegionAndMarkForDeleteFalse(UserAccountRole userRole,
      Region region);
}
