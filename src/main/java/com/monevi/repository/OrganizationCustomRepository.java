package com.monevi.repository;

import com.monevi.entity.Organization;
import com.monevi.model.GetOrganizationFilter;

import java.util.List;

public interface OrganizationCustomRepository {

  List<Organization> getOrganization(GetOrganizationFilter getOrganizationFilter);

}
