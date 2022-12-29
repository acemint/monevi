package com.monevi.repository;

import com.monevi.entity.OrganizationRegion;
import com.monevi.entity.OrganizationRegion_;
import com.monevi.entity.Report;
import com.monevi.entity.Report_;
import com.monevi.model.GetReportFilter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReportCustomRepositoryImpl
    extends BaseCustomRepository
    implements ReportCustomRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Optional<List<Report>> getReports(GetReportFilter filter) {
    CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<Report> reportCriteriaQuery = criteriaBuilder.createQuery(Report.class);
    Root<Report> reportRoot = reportCriteriaQuery.from(Report.class);

    reportCriteriaQuery
        .select(reportRoot)
        .where(
            this.predicateBuilder(
                criteriaBuilder, reportRoot, filter)
                .toArray(new Predicate[0]));

    this.sort(criteriaBuilder, reportCriteriaQuery, reportRoot, filter.getPageable());
    TypedQuery<Report> reportTypedQuery = this.entityManager.createQuery(reportCriteriaQuery);
    this.page(reportTypedQuery, filter.getPageable());
    return Optional.ofNullable(reportTypedQuery.getResultList());
  }

  private List<Predicate> predicateBuilder(
      CriteriaBuilder builder,
      Root<Report> root,
      GetReportFilter filter) {
    List<Predicate> predicates = new ArrayList<>();
    predicates.add(builder.isFalse(root.get(Report_.markForDelete)));
    if (Objects.nonNull(filter.getOrganizationRegionId())) {
      Join<Report, OrganizationRegion> reportJoin =
          root.join(Report_.organizationRegion);
      predicates.add(builder.isFalse(reportJoin.get(OrganizationRegion_.markForDelete)));
      predicates.add(builder.equal(reportJoin.get(OrganizationRegion_.id),
          filter.getOrganizationRegionId()));
    }
    if (Objects.nonNull(filter.getStartDate())
        && Objects.nonNull(filter.getEndDate())) {
      DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy");
      DateTime start = dateTimeFormatter.parseDateTime(filter.getStartDate());
      DateTime end = dateTimeFormatter.parseDateTime(filter.getEndDate());
      predicates.add(builder.between(
          root.get(Report_.periodDate),
          new Timestamp(start.getMillis()), new Timestamp(end.getMillis())));
    }
    return predicates;
  }

}
