package com.monevi.repository;

import com.monevi.entity.OrganizationRegion;
import com.monevi.entity.OrganizationRegion_;
import com.monevi.entity.Program;
import com.monevi.entity.Program_;
import com.monevi.model.GetProgramFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ProgramCustomRepositoryImpl
    extends BaseCustomRepository
    implements ProgramCustomRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Optional<List<Program>> getPrograms(GetProgramFilter filter) {
    CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<Program> programCriteriaQuery = criteriaBuilder.createQuery(Program.class);
    Root<Program> programRoot = programCriteriaQuery.from(Program.class);

    programCriteriaQuery
        .select(programRoot)
        .where(
            this.predicateBuilder(criteriaBuilder, programRoot, filter)
                .toArray(new Predicate[0]));

    this.sort(criteriaBuilder, programCriteriaQuery, programRoot, filter.getPageable());
    TypedQuery<Program> programTypedQuery = this.entityManager.createQuery(programCriteriaQuery);
    this.page(programTypedQuery, filter.getPageable());
    return Optional.ofNullable(programTypedQuery.getResultList());
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
    return predicates;
  }
}
