package com.ttt.mar.leads.filter;

import com.ttt.mar.leads.entities.DistributeApi;
import com.ttt.mar.leads.entities.DistributeApiStatus;
import com.ttt.mar.leads.entities.ProjectDistribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
public class DistributeApiFilter {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(DistributeApiFilter.class);

  public Specification<DistributeApi> filter(
      DistributeApiStatus status, String search, Map<String, String> sort) {
    logger.info("--- LeadDistributeFilter() ---");
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (StringUtils.isNotBlank(search)) {
        String searchValue = "%" + search.toLowerCase() + "%";
        predicates.add(criteriaBuilder.like(root.get("name"), searchValue));
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

  public Specification<DistributeApi> checkExistFilter(Integer distributeId, String name) {
    logger.info("--- checkExistFilter() ---");
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (distributeId != null) {
        predicates.add(criteriaBuilder.equal(root.get("id"), distributeId).not());
      }
      if (StringUtils.isNotBlank(name)) {
        predicates.add(criteriaBuilder.equal(root.get("name"), name));
      }
      predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
      criteriaQuery.distinct(true);
      return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    };
  }

  public Specification<DistributeApi> filterNotExistProject(Set<Integer> ids,
      DistributeApiStatus status) {
    logger.info("--- filterNotExistProject() ---");
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
