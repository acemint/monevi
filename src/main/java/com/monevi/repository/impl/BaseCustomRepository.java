package com.monevi.repository.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class BaseCustomRepository {

  void sort(
      CriteriaBuilder builder,
      CriteriaQuery<?> query,
      Root<?> root,
      Pageable pageable) {
    if (Objects.isNull(pageable)) {
      return;
    }
    List<Order> sortOrders = pageable.getSort().get()
        .map(o -> this.buildSortByAndSortDirection(builder, root, o))
        .collect(Collectors.toList());
    query.orderBy(sortOrders);
  }

  private Order buildSortByAndSortDirection(
      CriteriaBuilder criteriaBuilder,
      Root<?> root,
      Sort.Order sortOrder) {
    if (sortOrder.isDescending()) {
      return criteriaBuilder.desc(root.get(sortOrder.getProperty()));
    }
    else {
      return criteriaBuilder.asc(root.get(sortOrder.getProperty()));
    }
  }

  void page(
      TypedQuery<?> typedQuery,
      Pageable pageable) {
    if (Objects.isNull(pageable)) {
      return;
    }
    typedQuery.setFirstResult((int) pageable.getOffset())
        .setMaxResults(pageable.getPageSize());
  }

}
