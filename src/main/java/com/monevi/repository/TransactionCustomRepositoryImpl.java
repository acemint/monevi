package com.monevi.repository;


import com.monevi.entity.OrganizationRegion;
import com.monevi.entity.OrganizationRegion_;
import com.monevi.entity.Transaction;
import com.monevi.entity.Transaction_;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetTransactionFilter;
import com.monevi.util.DateUtils;

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

public class TransactionCustomRepositoryImpl
    extends BaseCustomRepository
    implements TransactionCustomRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Optional<List<Transaction>> getTransactions(GetTransactionFilter filter) throws ApplicationException {
    CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<Transaction> transactionCriteriaQuery = criteriaBuilder.createQuery(Transaction.class);
    Root<Transaction> transactionRoot = transactionCriteriaQuery.from(Transaction.class);

    transactionCriteriaQuery
        .select(transactionRoot)
        .where(
            this.predicateBuilder(criteriaBuilder, transactionRoot, filter)
                .toArray(new Predicate[0]));

    this.sort(criteriaBuilder, transactionCriteriaQuery, transactionRoot, filter.getPageable());
    TypedQuery<Transaction> transactionTypedQuery = this.entityManager.createQuery(transactionCriteriaQuery);
    this.page(transactionTypedQuery, filter.getPageable());
    return Optional.ofNullable(transactionTypedQuery.getResultList());
  }

  private List<Predicate> predicateBuilder(
      CriteriaBuilder builder,
      Root<Transaction> root,
      GetTransactionFilter filter) throws ApplicationException {
    List<Predicate> predicates = new ArrayList<>();
    predicates.add(builder.isFalse(root.get(Transaction_.markForDelete)));
    if (Objects.nonNull(filter.getOrganizationRegionId())) {
      Join<Transaction, OrganizationRegion> transactionJoin =
          root.join(Transaction_.organizationRegion);
      predicates.add(builder.isFalse(transactionJoin.get(OrganizationRegion_.markForDelete)));
      predicates.add(builder.equal(transactionJoin.get(OrganizationRegion_.id),
          filter.getOrganizationRegionId()));
    }
    if (Objects.nonNull(filter.getStartDate())
        && Objects.nonNull(filter.getEndDate())) {
      predicates.add(builder.between(root.get(Transaction_.transactionDate),
          DateUtils.dateInputToTimestamp(filter.getStartDate()),
          DateUtils.dateInputToTimestamp(filter.getEndDate())));
    }
    return predicates;
  }

}
