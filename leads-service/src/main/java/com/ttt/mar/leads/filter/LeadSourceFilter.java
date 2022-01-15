package com.ttt.mar.leads.filter;

import com.ttt.mar.leads.entities.LeadSource;
import com.ttt.mar.leads.entities.LeadSourceStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
@Component
public class LeadSourceFilter {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(LeadSourceFilter.class);

  public Specification<LeadSource> filter(LeadSourceStatus status, String search,
      Map<String, String> sort) {
    logger.info("--- LeadSourceFilter() ---");
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (StringUtils.isNotBlank(search)) {
        String searchValue = "%" + search.toLowerCase() + "%";
        Predicate pr1 = criteriaBuilder.like(root.get("name"), searchValue);
        Predicate pr2 = criteriaBuilder.like(root.get("source"), searchValue);
        Predicate pr3 = criteriaBuilder.like(root.get("utmSource"), searchValue);

        predicates.add(criteriaBuilder.or(pr1, pr2, pr3));
      }
      if (status != null) {
        predicates.add(criteriaBuilder.equal(root.get("status"), status));
      }
      if (sort != null && !sort.isEmpty()) {
        List<Order> orderList = new ArrayList<>();
        Set<String> keySet = sort.keySet();
        for (String key : keySet) {
          String orderType = sort.get(key);
          switch (key) {
            default:
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(root.get(key)));
              } else {
                orderList.add(criteriaBuilder.desc(root.get(key)));
              }
          }
        }
        criteriaQuery.orderBy(orderList);
      }
      predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
      criteriaQuery.distinct(true);
      return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    };
  }

  public Specification<LeadSource> checkExistFilter(Integer leadSourceId,
      String name, String source, String utmSource) {
    logger.info("--- checkExistFilter() ---");
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (leadSourceId != null) {
        predicates.add(criteriaBuilder.equal(root.get("id"), leadSourceId).not());
      }
      if (StringUtils.isNotBlank(name)) {
        predicates.add(criteriaBuilder.equal(root.get("name"), name));
      }
      if (StringUtils.isNotBlank(source)) {
        predicates.add(criteriaBuilder.equal(root.get("source"), source));
      }
      if (StringUtils.isNotBlank(utmSource)) {
        predicates.add(criteriaBuilder.equal(root.get("utmSource"), utmSource));
      }
      predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
      criteriaQuery.distinct(true);
      return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    };
  }

  public Specification<LeadSource> filterNotExistProject(Set<Integer> ids,
      LeadSourceStatus status) {
    logger.info("--- filterNotByProject() ---");
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (ids != null && !ids.isEmpty()) {
        predicates.add(criteriaBuilder.in(root.get("id")).value(ids).not());
      }
      if (status != null) {
        predicates.add(criteriaBuilder.equal(root.get("status"), status));
      }
      predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
      criteriaQuery.distinct(true);
      return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    };
  }
}
