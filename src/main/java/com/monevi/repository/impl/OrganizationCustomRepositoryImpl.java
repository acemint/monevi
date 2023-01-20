package com.monevi.repository.impl;

import com.monevi.entity.Organization;
import com.monevi.entity.OrganizationRegion;
import com.monevi.entity.OrganizationRegion_;
import com.monevi.entity.Organization_;
import com.monevi.entity.Program;
import com.monevi.entity.Program_;
import com.monevi.entity.Region;
import com.monevi.entity.Region_;
import com.monevi.model.GetOrganizationFilter;
import com.monevi.model.GetOrganizationWithProgramExistsFilter;
import com.monevi.repository.OrganizationCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class OrganizationCustomRepositoryImpl
    extends BaseCustomRepository
    implements OrganizationCustomRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Page<Organization> getOrganization(GetOrganizationFilter filter) {
    CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<Organization> organizationCriteriaQuery = criteriaBuilder.createQuery(Organization.class);
    Root<Organization> organizationRoot = organizationCriteriaQuery.from(Organization.class);
    CriteriaQuery<Long> countOrganizationCriteriaQuery = criteriaBuilder.createQuery(Long.class);
    Root<Organization> countOrganizationRoot =
        countOrganizationCriteriaQuery.from(Organization.class);

    organizationCriteriaQuery
        .select(organizationRoot)
        .where(criteriaBuilder.and(
            this.predicateBuilder(
                criteriaBuilder, organizationRoot, filter)
                .toArray(new Predicate[0])));
    
    countOrganizationCriteriaQuery.select(criteriaBuilder.count(countOrganizationRoot))
        .where(criteriaBuilder
            .and(this.predicateBuilder(criteriaBuilder, countOrganizationRoot, filter)
                .toArray(new Predicate[0])));

    this.sort(criteriaBuilder, organizationCriteriaQuery, organizationRoot, filter.getPageable());
    TypedQuery<Organization> organizationTypedQuery =
        this.entityManager.createQuery(organizationCriteriaQuery);
    Long countOrganizationResult =
        this.entityManager.createQuery(countOrganizationCriteriaQuery).getSingleResult();
    this.page(organizationTypedQuery, filter.getPageable());
    try {
      return new PageImpl<>(organizationTypedQuery.getResultList(), filter.getPageable(),
          countOrganizationResult);
    } catch (Exception e) {
      return new PageImpl<>(Collections.emptyList(), filter.getPageable(), 0L);
    }
  }

  private List<Predicate> predicateBuilder(
      CriteriaBuilder builder,
      Root<Organization> root,
      GetOrganizationFilter filter) {
    List<Predicate> predicates = new ArrayList<>();
    predicates.add(builder.isFalse(root.get(Organization_.MARK_FOR_DELETE)));
    if (Objects.nonNull(filter.getRegionName())) {
      Join<Organization, OrganizationRegion> organizationRegion =
          root.join(Organization_.organizationRegions);
      Join<OrganizationRegion, Region> region =
          organizationRegion.join(OrganizationRegion_.region);
      predicates.add(builder.isFalse(organizationRegion.get(OrganizationRegion_.markForDelete)));
      predicates.add(builder.isFalse(region.get(Region_.markForDelete)));
      predicates.add(builder.equal(region.get(Region_.NAME), filter.getRegionName()));
    }
    if (Objects.nonNull(filter.getSearchTerm())) {
      List<Predicate> searchTermPredicate = new ArrayList<>();
      searchTermPredicate.add(builder.like(builder.lower(root.get(Organization_.name)),
          this.generateLikePattern(filter.getSearchTerm())));
      searchTermPredicate.add(builder.like(builder.lower(root.get(Organization_.abbreviation)),
          this.generateLikePattern(filter.getSearchTerm())));
      predicates.add(builder.or(searchTermPredicate.toArray(new Predicate[0])));
    }
    return predicates;
  }

  private String generateLikePattern(String input) {
    return "%" + input.toLowerCase() + "%";
  }

  @Override
  public Page<Organization> getOrganizationByRegionAndPeriodAndProgramExists(
      GetOrganizationWithProgramExistsFilter filter) {
    CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<Organization> organizationCriteriaQuery =
        criteriaBuilder.createQuery(Organization.class);
    Root<Organization> organizationRoot = organizationCriteriaQuery.from(Organization.class);
    CriteriaQuery<Long> countOrganizationCriteriaQuery = criteriaBuilder.createQuery(Long.class);
    Root<Organization> countOrganizationRoot =
        countOrganizationCriteriaQuery.from(Organization.class);

    organizationCriteriaQuery.select(organizationRoot)
        .distinct(true)
        .where(criteriaBuilder.and(this.predicateBuilder(criteriaBuilder, organizationRoot, filter)
            .toArray(new Predicate[0])));

    countOrganizationCriteriaQuery.select(criteriaBuilder.count(countOrganizationRoot))
        .distinct(true)
        .where(criteriaBuilder
            .and(this.predicateBuilder(criteriaBuilder, countOrganizationRoot, filter)
                .toArray(new Predicate[0])));

    this.sort(criteriaBuilder, organizationCriteriaQuery, organizationRoot, filter.getPageable());
    TypedQuery<Organization> organizationTypedQuery =
        this.entityManager.createQuery(organizationCriteriaQuery);
    Long countOrganizationResult =
        this.entityManager.createQuery(countOrganizationCriteriaQuery).getSingleResult();
    this.page(organizationTypedQuery, filter.getPageable());
    try {
      return new PageImpl<>(organizationTypedQuery.getResultList(), filter.getPageable(),
          countOrganizationResult);
    } catch (Exception e) {
      return new PageImpl<>(Collections.emptyList(), filter.getPageable(), 0L);
    }
  }
  
  private List<Predicate> predicateBuilder(CriteriaBuilder builder, Root<Organization> root,
      GetOrganizationWithProgramExistsFilter filter) {
    List<Predicate> predicates = new ArrayList<>();
    predicates.add(builder.isFalse(root.get(Organization_.MARK_FOR_DELETE)));

    Join<Organization, OrganizationRegion> organizationRegion =
        root.join(Organization_.organizationRegions);
    Join<OrganizationRegion, Region> region = organizationRegion.join(OrganizationRegion_.region);
    predicates.add(builder.isFalse(organizationRegion.get(OrganizationRegion_.markForDelete)));
    predicates.add(builder.isFalse(region.get(Region_.markForDelete)));
    predicates.add(builder.equal(region.get(Region_.id), filter.getRegionId()));
    Join<OrganizationRegion, Program> program = organizationRegion.join(OrganizationRegion_.programs);
    predicates.add(builder.equal(program.get(Program_.PERIOD_YEAR), filter.getPeriodYear()));

    return predicates;
  }
}
