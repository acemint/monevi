package com.monevi.repository;

import com.monevi.entity.Organization;
import com.monevi.model.GetOrganizationFilter;

import java.util.List;
import java.util.Optional;

public interface OrganizationCustomRepository {

  Optional<List<Organization>> getOrganization(GetOrganizationFilter filter);

}
