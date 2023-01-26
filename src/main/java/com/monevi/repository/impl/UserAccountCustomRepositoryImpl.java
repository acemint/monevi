package com.monevi.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.monevi.entity.Organization;
import com.monevi.entity.OrganizationRegion;
import com.monevi.entity.OrganizationRegion_;
import com.monevi.entity.Organization_;
import com.monevi.entity.Region;
import com.monevi.entity.Region_;
import com.monevi.entity.UserAccount;
import com.monevi.entity.UserAccount_;
import com.monevi.enums.UserAccountRole;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetStudentFilter;
import com.monevi.repository.UserAccountCustomRepository;

public class UserAccountCustomRepositoryImpl extends BaseCustomRepository
    implements UserAccountCustomRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Optional<UserAccount> findAssignedUserByOrganizationRegionIdAndRoleAndMarkForDeleteFalse(
      Integer periodMonth, Integer periodYear, String organizationRegionId, UserAccountRole role)
      throws ApplicationException {
    CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<UserAccount> userAccountCriteriaQuery =
        criteriaBuilder.createQuery(UserAccount.class);
    Root<UserAccount> userAccountRoot = userAccountCriteriaQuery.from(UserAccount.class);

    userAccountCriteriaQuery.select(userAccountRoot)
        .where(this.predicateBuilder(criteriaBuilder, userAccountRoot, periodMonth, periodYear,
            organizationRegionId, role, Boolean.FALSE).toArray(new Predicate[0]));

    TypedQuery<UserAccount> userAccountTypedQuery =
        this.entityManager.createQuery(userAccountCriteriaQuery);

    try {
      return Optional.ofNullable(userAccountTypedQuery.getSingleResult());
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  private List<Predicate> predicateBuilder(CriteriaBuilder builder, Root<UserAccount> root,
      Integer periodMonth, Integer periodYear, String organizationRegionId, UserAccountRole role,
      Boolean isLockedAccount) throws ApplicationException {
    List<Predicate> predicates = new ArrayList<>();
    predicates.add(builder.isFalse(root.get(UserAccount_.MARK_FOR_DELETE)));
    predicates.add(builder.equal(root.get(UserAccount_.LOCKED_ACCOUNT), isLockedAccount));
    predicates.add(builder.equal(root.get(UserAccount_.ROLE), role));
    predicates.add(builder.equal(root.get(UserAccount_.PERIOD_YEAR), periodYear));

    if (Objects.nonNull(periodMonth)) {
      predicates.add(builder.equal(root.get(UserAccount_.PERIOD_MONTH), periodMonth));
    }

    Join<UserAccount, OrganizationRegion> organizationRegionJoin =
        root.join(UserAccount_.ORGANIZATION_REGION);
    predicates
        .add(builder.isFalse(organizationRegionJoin.get(OrganizationRegion_.MARK_FOR_DELETE)));
    predicates.add(
        builder.equal(organizationRegionJoin.get(OrganizationRegion_.id), organizationRegionId));

    return predicates;
  }

  @Override
  public Optional<List<UserAccount>> findAllByOrganizationRegionIdAndRoleAndMarkForDeleteFalse(
      Integer periodMonth, Integer periodYear, String organizationRegionId, UserAccountRole role)
      throws ApplicationException {
    CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<UserAccount> userAccountCriteriaQuery =
        criteriaBuilder.createQuery(UserAccount.class);
    Root<UserAccount> userAccountRoot = userAccountCriteriaQuery.from(UserAccount.class);

    userAccountCriteriaQuery.select(userAccountRoot)
        .where(this.predicateBuilder(criteriaBuilder, userAccountRoot, periodMonth, periodYear,
            organizationRegionId, role, Boolean.TRUE).toArray(new Predicate[0]));

    TypedQuery<UserAccount> userAccountTypedQuery =
        this.entityManager.createQuery(userAccountCriteriaQuery);

    try {
      return Optional.ofNullable(userAccountTypedQuery.getResultList());
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  @Override
  public Page<UserAccount> findAllStudentByFilter(GetStudentFilter filter)
      throws ApplicationException {
    CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<UserAccount> userAccountCriteriaQuery =
        criteriaBuilder.createQuery(UserAccount.class);
    Root<UserAccount> userAccountRoot = userAccountCriteriaQuery.from(UserAccount.class);
    CriteriaQuery<Long> countUserAccountCriteriaQuery = criteriaBuilder.createQuery(Long.class);
    Root<UserAccount> countUserAccountRoot = countUserAccountCriteriaQuery.from(UserAccount.class);

    userAccountCriteriaQuery.select(userAccountRoot).where(
        this.predicateBuilder(criteriaBuilder, userAccountRoot, filter).toArray(new Predicate[0]));
    countUserAccountCriteriaQuery.select(criteriaBuilder.count(countUserAccountRoot)).where(this
        .predicateBuilder(criteriaBuilder, countUserAccountRoot, filter).toArray(new Predicate[0]));

    this.sort(criteriaBuilder, userAccountCriteriaQuery, userAccountRoot, filter.getPageable());
    TypedQuery<UserAccount> userAccountTypedQuery =
        this.entityManager.createQuery(userAccountCriteriaQuery);
    Long countUserAccountResult =
        this.entityManager.createQuery(countUserAccountCriteriaQuery).getSingleResult();
    this.page(userAccountTypedQuery, filter.getPageable());
    try {
      return new PageImpl<>(userAccountTypedQuery.getResultList(), filter.getPageable(),
          countUserAccountResult);
    } catch (Exception e) {
      return new PageImpl<>(Collections.emptyList(), filter.getPageable(), 0L);
    }
  }

  private List<Predicate> predicateBuilder(CriteriaBuilder builder, Root<UserAccount> root,
      GetStudentFilter filter) throws ApplicationException {
    List<Predicate> predicates = new ArrayList<>();
    predicates.add(builder.isFalse(root.get(UserAccount_.MARK_FOR_DELETE)));

    if (Objects.nonNull(filter.getStudentName())) {
      predicates.add(builder.like(builder.lower(root.get(UserAccount_.FULL_NAME)),
          generateLikePattern(filter.getStudentName())));
    }
    if (Objects.nonNull(filter.getLockedAccount())) {
      predicates
          .add(builder.equal(root.get(UserAccount_.LOCKED_ACCOUNT), filter.getLockedAccount()));
    }
    if (Objects.nonNull(filter.getStudentRole())) {
      predicates.add(builder.equal(root.get(UserAccount_.ROLE), filter.getStudentRole()));
    } else {
      predicates.add(builder.notEqual(root.get(UserAccount_.ROLE), UserAccountRole.SUPERVISOR));
    }
    if (Objects.nonNull(filter.getPeriodMonth())) {
      predicates.add(builder.equal(root.get(UserAccount_.PERIOD_MONTH), filter.getPeriodMonth()));
    }
    if (Objects.nonNull(filter.getPeriodYear())) {
      predicates.add(builder.equal(root.get(UserAccount_.PERIOD_YEAR), filter.getPeriodYear()));
    }
    if (Objects.nonNull(filter.getOrganizationName())) {
      Join<UserAccount, OrganizationRegion> organizationRegionJoin =
          root.join(UserAccount_.ORGANIZATION_REGION);
      Join<OrganizationRegion, Organization> organizationRegionOrganizationJoin =
          organizationRegionJoin.join(OrganizationRegion_.ORGANIZATION);
      predicates
          .add(builder.isFalse(organizationRegionJoin.get(OrganizationRegion_.MARK_FOR_DELETE)));
      predicates.add(
          builder.isFalse(organizationRegionOrganizationJoin.get(Organization_.MARK_FOR_DELETE)));
      predicates.add(
          builder.like(builder.lower(organizationRegionOrganizationJoin.get(Organization_.NAME)),
              generateLikePattern(filter.getOrganizationName())));
    }
    if (Objects.nonNull(filter.getRegionId())) {
      Join<UserAccount, OrganizationRegion> organizationRegionJoin =
          root.join(UserAccount_.ORGANIZATION_REGION);
      Join<OrganizationRegion, Region> organizationRegionRegionJoin =
          organizationRegionJoin.join(OrganizationRegion_.REGION);
      predicates
          .add(builder.isFalse(organizationRegionJoin.get(OrganizationRegion_.MARK_FOR_DELETE)));
      predicates.add(builder.isFalse(organizationRegionRegionJoin.get(Region_.MARK_FOR_DELETE)));
      predicates.add(
          builder.equal(organizationRegionRegionJoin.get(Region_.ID), filter.getRegionId()));
    }
    return predicates;
  }

  private String generateLikePattern(String input) {
    return "%" + input.toLowerCase() + "%";
  }
}
