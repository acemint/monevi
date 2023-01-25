package com.monevi.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.monevi.dto.response.ReportHistoryFindResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.monevi.entity.Organization;
import com.monevi.entity.OrganizationRegion;
import com.monevi.entity.OrganizationRegion_;
import com.monevi.entity.Organization_;
import com.monevi.entity.Region;
import com.monevi.entity.Region_;
import com.monevi.entity.Report;
import com.monevi.entity.ReportHistory;
import com.monevi.entity.ReportHistory_;
import com.monevi.entity.Report_;
import com.monevi.entity.UserAccount;
import com.monevi.entity.UserAccount_;
import com.monevi.enums.UserAccountRole;
import com.monevi.exception.ApplicationException;
import com.monevi.model.GetReportHistoryFilter;
import com.monevi.repository.ReportHistoryCustomRepository;

public class ReportHistoryCustomRepositoryImpl extends BaseCustomRepository
    implements ReportHistoryCustomRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Page<Tuple> findReportHistoryByUser(GetReportHistoryFilter filter)
      throws ApplicationException {
    CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
    Root<ReportHistory> reportHistoryRoot = criteriaQuery.from(ReportHistory.class);
    Join<ReportHistory, Report> report = reportHistoryRoot.join(ReportHistory_.report);
    Join<ReportHistory, UserAccount> userAccount = reportHistoryRoot.join(ReportHistory_.user);
    Join<Report, OrganizationRegion> organizationRegion = report.join(Report_.ORGANIZATION_REGION);
    Join<OrganizationRegion, Organization> organization =
        organizationRegion.join(OrganizationRegion_.organization);
    
    CriteriaQuery<Long> countReportHistoryQuery = criteriaBuilder.createQuery(Long.class);
    Root<ReportHistory> countReportHistoryRoot = countReportHistoryQuery.from(ReportHistory.class);

    criteriaQuery
        .multiselect(organization.get(Organization_.ABBREVIATION), report.get(Report_.PERIOD_DATE),
            reportHistoryRoot.get(ReportHistory_.REMARKS), userAccount.get(UserAccount_.FULL_NAME),
            reportHistoryRoot.get(ReportHistory_.CREATED_DATE))
        .where(this.predicateBuilder(criteriaBuilder, reportHistoryRoot, filter)
            .toArray(new Predicate[0]));
    
    countReportHistoryQuery.select(criteriaBuilder.count(countReportHistoryRoot))
        .where(this.predicateBuilder(criteriaBuilder, countReportHistoryRoot, filter)
            .toArray(new Predicate[0]));

    this.sort(criteriaBuilder, criteriaQuery, reportHistoryRoot, filter.getPageable());
    TypedQuery<Tuple> reportHistoryTypedQuery = this.entityManager.createQuery(criteriaQuery);
    Long countReportHistoryResult =
        this.entityManager.createQuery(countReportHistoryQuery).getSingleResult();
    this.page(reportHistoryTypedQuery, filter.getPageable());
    try {
      return new PageImpl<>(reportHistoryTypedQuery.getResultList(), filter.getPageable(),
          countReportHistoryResult);
    } catch (Exception e) {
      return new PageImpl<>(Collections.emptyList(), filter.getPageable(), 0L);
    }
  }

  private List<Predicate> predicateBuilder(CriteriaBuilder builder, Root<ReportHistory> root,
      GetReportHistoryFilter filter) throws ApplicationException {
    List<Predicate> predicates = new ArrayList<>();
    predicates.add(builder.isFalse(root.get(ReportHistory_.MARK_FOR_DELETE)));

    Join<ReportHistory, Report> report = root.join(ReportHistory_.report);
    Join<ReportHistory, UserAccount> userAccount = root.join(ReportHistory_.user);
    Join<Report, OrganizationRegion> organizationRegion = report.join(Report_.organizationRegion);
    if (UserAccountRole.TREASURER.equals(filter.getUserRole())
        || UserAccountRole.CHAIRMAN.equals(filter.getUserRole())) {
      predicates.add(builder.equal(organizationRegion.get(OrganizationRegion_.id),
          filter.getOrganizationRegionId()));
      predicates.add(builder.equal(report.get(Report_.termOfOffice), filter.getTermOfOffice()));
      predicates.add(builder.notEqual(userAccount.get(UserAccount_.role), filter.getUserRole()));
    } else if (UserAccountRole.SUPERVISOR.equals(filter.getUserRole())) {
      Join<OrganizationRegion, Region> region = organizationRegion.join(OrganizationRegion_.REGION);
      predicates.add(builder.equal(region.get(Region_.id), filter.getRegionId()));
      predicates
          .add(builder.notEqual(userAccount.get(UserAccount_.role), UserAccountRole.TREASURER));
      predicates.add(builder.notEqual(userAccount.get(UserAccount_.id), filter.getUserId()));
    }
    return predicates;
  }
}
