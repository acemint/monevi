package com.monevi.repository;

import java.util.List;
import java.util.Optional;

import com.monevi.entity.UserAccount;
import com.monevi.enums.UserAccountRole;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetStudentFilter;

public interface UserAccountCustomRepository {

  Optional<UserAccount> findAssignedUserByOrganizationRegionIdAndRoleAndMarkForDeleteFalse(
      Integer periodMonth, Integer periodYear, String organizationRegionId, UserAccountRole role)
      throws ApplicationException;

  Optional<List<UserAccount>> findAllByOrganizationRegionIdAndRoleAndMarkForDeleteFalse(
      Integer periodMonth, Integer periodYear, String organizationRegionId, UserAccountRole role)
      throws ApplicationException;

  Optional<List<UserAccount>> findAllStudentByFilter(GetStudentFilter filter)
      throws ApplicationException;
}
