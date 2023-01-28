package com.monevi.repository.impl;


import com.monevi.entity.OrganizationRegion;
import com.monevi.entity.OrganizationRegion_;
import com.monevi.entity.Region;
import com.monevi.entity.Region_;
import com.monevi.entity.Transaction;
import com.monevi.entity.Transaction_;
import com.monevi.enums.EntryPosition;
import com.monevi.enums.GeneralLedgerAccountType;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetTransactionFilter;
import com.monevi.repository.TransactionCustomRepository;
import com.monevi.util.DateUtils;
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

public class TransactionCustomRepositoryImpl
    extends BaseCustomRepository
    implements TransactionCustomRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Page<Transaction> getTransactions(GetTransactionFilter filter) throws ApplicationException {
    CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<Transaction> transactionCriteriaQuery = criteriaBuilder.createQuery(Transaction.class);
    Root<Transaction> transactionRoot = transactionCriteriaQuery.from(Transaction.class);
    CriteriaQuery<Long> countTransactionCriteriaQuery = criteriaBuilder.createQuery(Long.class);
    Root<Transaction> countTransactionRoot = countTransactionCriteriaQuery.from(Transaction.class);

    transactionCriteriaQuery
        .select(transactionRoot)
        .where(
            this.predicateBuilder(criteriaBuilder, transactionRoot, filter)
                .toArray(new Predicate[0]));
    
    countTransactionCriteriaQuery
        .select(criteriaBuilder.count(countTransactionRoot))
        .where(this.predicateBuilder(criteriaBuilder, countTransactionRoot, filter)
            .toArray(new Predicate[0]));

    this.sort(criteriaBuilder, transactionCriteriaQuery, transactionRoot, filter.getPageable());
    TypedQuery<Transaction> transactionTypedQuery =
        this.entityManager.createQuery(transactionCriteriaQuery);
    Long countTransactionResult =
        this.entityManager.createQuery(countTransactionCriteriaQuery).getSingleResult();
    this.page(transactionTypedQuery, filter.getPageable());
    try {
      return new PageImpl<>(transactionTypedQuery.getResultList(), filter.getPageable(),
          countTransactionResult);
    } catch (Exception e) {
      return new PageImpl<>(Collections.emptyList(), filter.getPageable(), 0L);
    }
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
    if (Objects.nonNull(filter.getRegionId())) {
      Join<Transaction, OrganizationRegion> transactionJoin =
          root.join(Transaction_.organizationRegion);
      Join<OrganizationRegion, Region> regionJoin =
          transactionJoin.join(OrganizationRegion_.REGION);
      predicates.add(builder.isFalse(transactionJoin.get(OrganizationRegion_.markForDelete)));
      predicates
          .add(builder.equal(regionJoin.get(Region_.ID), filter.getRegionId()));
    }
    if (Objects.nonNull(filter.getEntryPosition())) {
      predicates
          .add(builder.equal(root.get(Transaction_.ENTRY_POSITION), filter.getEntryPosition()));
    }
    if (Objects.nonNull(filter.getGeneralLedgerAccountType())) {
      predicates.add(builder.equal(root.get(Transaction_.GENERAL_LEDGER_ACCOUNT_TYPE),
          filter.getGeneralLedgerAccountType()));
    }
    if (Objects.nonNull(filter.getTransactionType())) {
      predicates.add(builder.equal(root.get(Transaction_.TYPE), filter.getTransactionType()));
    }
    return predicates;
  }

  @Override
  public Double calculateTotalByGeneralLedgerAccountTypeAndOrganizationRegionId(
      GeneralLedgerAccountType type, String organizationRegionId) throws ApplicationException {
    CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<Double> transactionCriteriaQuery = builder.createQuery(Double.class);
    Root<Transaction> transactionRoot = transactionCriteriaQuery.from(Transaction.class);

    transactionCriteriaQuery
        .select(builder.sum(builder.selectCase()
            .when(builder.equal(transactionRoot.get(Transaction_.ENTRY_POSITION),
                EntryPosition.CREDIT), builder.neg(transactionRoot.get(Transaction_.amount)))
            .when(builder.equal(transactionRoot.get(Transaction_.ENTRY_POSITION),
                EntryPosition.DEBIT), transactionRoot.get(Transaction_.amount))
            .otherwise(0).as(Double.class)))
        .where(
            builder.and(this.predicateBuilder(builder, transactionRoot, type, organizationRegionId)
                .toArray(new Predicate[0])));

    try {
      return Optional
          .ofNullable(this.entityManager.createQuery(transactionCriteriaQuery).getSingleResult())
          .orElse(0d);
    } catch (Exception e) {
      return 0d;
    }
  }

  private List<Predicate> predicateBuilder(CriteriaBuilder builder, Root<Transaction> root,
      GeneralLedgerAccountType type, String organizationRegionId) throws ApplicationException {
    List<Predicate> predicates = new ArrayList<>();
    Join<Transaction, OrganizationRegion> organizationRegionJoin =
        root.join(Transaction_.ORGANIZATION_REGION);
    predicates.add(
        builder.equal(organizationRegionJoin.get(OrganizationRegion_.id), organizationRegionId));
    predicates.add(builder.equal(root.get(Transaction_.generalLedgerAccountType), type));
    return predicates;
  }
}
