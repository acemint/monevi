package com.monevi.repository.impl;

import com.monevi.entity.OrganizationRegion;
import com.monevi.entity.OrganizationRegion_;
import com.monevi.entity.Program;
import com.monevi.entity.Program_;
import com.monevi.model.GetProgramFilter;
import com.monevi.repository.ProgramCustomRepository;
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

public class ProgramCustomRepositoryImpl
    extends BaseCustomRepository
    implements ProgramCustomRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Page<Program> getPrograms(GetProgramFilter filter) {
    CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<Program> programCriteriaQuery = criteriaBuilder.createQuery(Program.class);
    Root<Program> programRoot = programCriteriaQuery.from(Program.class);
    CriteriaQuery<Long> countProgramCriteriaQuery = criteriaBuilder.createQuery(Long.class);
    Root<Program> countProgramRoot = countProgramCriteriaQuery.from(Program.class);

    programCriteriaQuery
        .select(programRoot)
        .where(
            this.predicateBuilder(criteriaBuilder, programRoot, filter)
                .toArray(new Predicate[0]));
    
    countProgramCriteriaQuery.select(criteriaBuilder.count(countProgramRoot))
        .where(this.predicateBuilder(criteriaBuilder, countProgramRoot, filter)
            .toArray(new Predicate[0]));

    this.sort(criteriaBuilder, programCriteriaQuery, programRoot, filter.getPageable());
    TypedQuery<Program> programTypedQuery = this.entityManager.createQuery(programCriteriaQuery);
    Long countProgramResult =
        this.entityManager.createQuery(countProgramCriteriaQuery).getSingleResult();
    this.page(programTypedQuery, filter.getPageable());
    try {
      return new PageImpl<>(programTypedQuery.getResultList(), filter.getPageable(),
          countProgramResult);
    } catch (Exception e) {
      return new PageImpl<>(Collections.emptyList(), filter.getPageable(), 0L);
    }
  }

  private List<Predicate> predicateBuilder(
      CriteriaBuilder builder,
      Root<Program> root,
      GetProgramFilter filter) {
    List<Predicate> predicates = new ArrayList<>();
    predicates.add(builder.isFalse(root.get(Program_.markForDelete)));
    if (Objects.nonNull(filter.getOrganizationRegionId())) {
      Join<Program, OrganizationRegion> programJoin =
          root.join(Program_.organizationRegion);
      predicates.add(builder.isFalse(programJoin.get(OrganizationRegion_.markForDelete)));
      predicates.add(builder.equal(programJoin.get(OrganizationRegion_.id), filter.getOrganizationRegionId()));
    }
    if (Objects.nonNull(filter.getPeriodYear())) {
      predicates.add(builder.equal(root.get(Program_.PERIOD_YEAR), filter.getPeriodYear()));
    }
    return predicates;
  }
}
