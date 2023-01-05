package com.monevi.repository;

import java.util.Optional;

import com.monevi.entity.UserAccount;
import com.monevi.enums.UserAccountRole;
import com.monevi.exception.ApplicationException;

public interface UserAccountCustomRepository {

  Optional<UserAccount> findAssignedUserByOrganizationRegionIdAndRoleAndMarkForDeleteFalse(
      Integer periodMonth, Integer periodYear, String organizationRegionId, UserAccountRole role)
      throws ApplicationException;
}
