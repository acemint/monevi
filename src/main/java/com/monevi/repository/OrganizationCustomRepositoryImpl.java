package com.monevi.repository;

import com.monevi.entity.Organization;
import com.monevi.entity.OrganizationRegion;
import com.monevi.entity.OrganizationRegion_;
import com.monevi.entity.Organization_;
import com.monevi.entity.Region;
import com.monevi.entity.Region_;
import com.monevi.model.GetOrganizationFilter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrganizationCustomRepositoryImpl implements OrganizationCustomRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<Organization> getOrganization(GetOrganizationFilter getOrganizationFilter) {
    CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<Organization> organizationCriteriaQuery = criteriaBuilder.createQuery(Organization.class);
    Root<Organization> organizationRoot = organizationCriteriaQuery.from(Organization.class);

    organizationCriteriaQuery
        .select(organizationRoot)
        .where(criteriaBuilder.and(
            this.predicateBuilder(
                criteriaBuilder, organizationCriteriaQuery, organizationRoot, getOrganizationFilter)
                .toArray(new Predicate[0])));

    this.sort(criteriaBuilder, organizationCriteriaQuery, organizationRoot, getOrganizationFilter);
    TypedQuery<Organization> organizationTypedQuery = this.entityManager.createQuery(organizationCriteriaQuery);
    this.page(organizationTypedQuery, getOrganizationFilter);
    return organizationTypedQuery.getResultList();
  }

  private List<Predicate> predicateBuilder(
      CriteriaBuilder builder,
      CriteriaQuery<Organization> query,
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
    return predicates;
  }

  private void sort(
      CriteriaBuilder builder,
      CriteriaQuery<Organization> query,
      Root<Organization> root,
      GetOrganizationFilter filter) {
    Pageable pageable = filter.getPageable();
    if (Objects.isNull(pageable)) {
      return;
    }
    List<Order> sortOrders = pageable.getSort().get()
        .map(o -> this.sortDirection(builder, root, o))
        .collect(Collectors.toList());
    query.orderBy(sortOrders);
  }

  private Order sortDirection(
      CriteriaBuilder criteriaBuilder,
      Root<Organization> organizationRoot,
      Sort.Order sortOrder) {
    if (sortOrder.isDescending()) {
       return criteriaBuilder.desc(organizationRoot.get(sortOrder.getProperty()));
    }
    else {
      return criteriaBuilder.asc(organizationRoot.get(sortOrder.getProperty()));
    }
  }

  private void page(
      TypedQuery<Organization> typedQuery,
      GetOrganizationFilter filter) {
    Pageable pageable = filter.getPageable();
    if (Objects.isNull(pageable)) {
      return;
    }
    typedQuery.setFirstResult((int) pageable.getOffset())
        .setMaxResults(pageable.getPageSize());
  }
}
