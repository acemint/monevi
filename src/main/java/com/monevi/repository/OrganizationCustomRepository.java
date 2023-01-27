package com.monevi.repository;

import com.monevi.entity.Organization;
import com.monevi.model.GetOrganizationFilter;
import com.monevi.model.GetOrganizationWithProgramExistsFilter;
import org.springframework.data.domain.Page;

public interface OrganizationCustomRepository {

  Page<Organization> getOrganization(GetOrganizationFilter filter);
  
  Page<Organization> getOrganizationByRegionAndPeriodAndProgramExists(
      GetOrganizationWithProgramExistsFilter filter);

}
