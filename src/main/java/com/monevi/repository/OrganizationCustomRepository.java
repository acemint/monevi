package com.monevi.repository;

import com.monevi.entity.Organization;
import com.monevi.model.GetOrganizationFilter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface OrganizationCustomRepository {

  Page<Organization> getOrganization(GetOrganizationFilter filter);

}
