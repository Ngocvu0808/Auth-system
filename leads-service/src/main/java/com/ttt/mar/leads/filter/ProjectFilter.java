package com.ttt.mar.leads.filter;

import com.ttt.mar.leads.entities.Project;
import com.ttt.mar.leads.entities.ProjectStatus;
import com.ttt.mar.leads.utils.Constants;
import com.ttt.mar.leads.utils.Utils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * @author bontk
 * @created_date 19/04/2021
 */
@Component
public class ProjectFilter {

  private static final Logger logger = LoggerFactory.getLogger(ProjectFilter.class);


  public Specification<Project> filter(Long startDate, Long endDate, ProjectStatus status,
      String search, Map<String, String> sort) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (StringUtils.isNotBlank(search)) {
        String searchConvert = Utils.textSpecialCharacters(search);
        String searchValue = "%" + searchConvert.toLowerCase() + "%";
        Predicate pr1 = criteriaBuilder.like(root.get("name"), searchValue);
        Predicate pr2 = criteriaBuilder.like(root.get("code"), searchValue);
        Predicate pr3 = criteriaBuilder.like(root.get("partnerCode"), searchValue);

        predicates.add(criteriaBuilder.or(pr1, pr2, pr3));
      }
      if (status != null) {
        predicates.add(criteriaBuilder.equal(root.get("status"), status));
      }
      if (startDate != null) {
        predicates
            .add(criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), new Date(startDate)));
      }
      if (endDate != null) {
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), new Date(endDate)));
      }
      if (sort != null && !sort.isEmpty()) {
        List<Order> orderList = new ArrayList<>();
        Set<String> keySet = sort.keySet();
        for (String key : keySet) {
          String orderType = sort.get(key);
          if (orderType.equals(Constants.SORT_ASC)) {
            orderList.add(criteriaBuilder.asc(root.get(key)));
          } else if (orderType.equals(Constants.SORT_DESC)) {
            orderList.add(criteriaBuilder.desc(root.get(key)));
          }
        }
        criteriaQuery.orderBy(orderList);
      }
      predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
      criteriaQuery.distinct(true);
      return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    };
  }

  public Specification<Project> filterExpired(ProjectStatus status, Long expiredTime) {
    logger.info("--- filterProjectExpired() ---");
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (status != null) {
        predicates.add(criteriaBuilder.equal(root.get("status"), status));
      }
      if (expiredTime != null) {
        predicates.add(criteriaBuilder.lessThan(root.get("endDate"), new Date(expiredTime)));
      }
      predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
      criteriaQuery.distinct(true);
      return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    };
  }
}
