package com.monevi.repository;

import com.monevi.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, String>, OrganizationCustomRepository {

  String GET_ORGANIZATIONS_WITH_REPORT =
      "SELECT DISTINCT mr.id as report_id, mo.id as organization_region_id, mo.name as organization_name, mo.abbreviation as organization_abbreviation, mr.period_date, mr.status FROM monevi_report mr "
          + "JOIN monevi_organization_region mor ON mr.organization_region_id = mor.id "
          + "JOIN monevi_region mr2 ON mr2.id = mor.region_id "
          + "JOIN monevi_organization mo ON mo.id = mor.organization_id "
          + "WHERE mr2.id = :regionId "
          + "AND mr.mark_for_delete IS FALSE "
          + "AND mor.mark_for_delete IS FALSE "
          + "AND mr2.mark_for_delete IS FALSE";

  String GET_ORGANIZATION_WITH_PROGRAM =
      "SELECT DISTINCT mo.id as organization_region_id, mo.name as organization_name, mo.abbreviation as organization_abbreviation, mp.period_year FROM monevi_program mp "
          + "JOIN monevi_organization_region mor ON mp.organization_region_id = mor.id "
          + "JOIN monevi_region mr2 ON mr2.id = mor.region_id "
          + "JOIN monevi_organization mo ON mo.id = mor.organization_id "
          + "WHERE mr2.id = :regionId "
          + "AND mp.mark_for_delete IS FALSE "
          + "AND mor.mark_for_delete IS FALSE "
          + "AND mr2.mark_for_delete IS FALSE";

  Optional<Organization> findByNameAndMarkForDeleteIsFalse(String name);

  @Query(value = GET_ORGANIZATIONS_WITH_REPORT, nativeQuery = true)
  List<Tuple> getOrganizationsWithReport(@Param("regionId") String regionId);

  @Query(value = GET_ORGANIZATION_WITH_PROGRAM, nativeQuery = true)
  List<Tuple> getOrganizationsWithProgram(@Param("regionId") String regionId);
}
