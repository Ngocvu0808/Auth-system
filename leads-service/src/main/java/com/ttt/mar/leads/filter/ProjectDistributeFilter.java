package com.ttt.mar.leads.filter;

import com.ttt.mar.leads.entities.ProjectDistribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
@Component
public class ProjectDistributeFilter {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(ProjectDistributeFilter.class);

  public Specification<ProjectDistribute> filter(Integer projectId, Map<String, String> sort) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      predicates.add(criteriaBuilder.equal(root.get("projectId"), projectId));
      Join<Object, Object> distributeApi = root.join("distributeApi", JoinType.LEFT);
      predicates.add(criteriaBuilder.equal(distributeApi.get("isDeleted"), false));

      if (sort != null && !sort.isEmpty()) {
        List<Order> orderList = new ArrayList<>();
        Set<String> keySet = sort.keySet();
        for (String key : keySet) {
          String orderType = sort.get(key);
          switch (key) {
            case "name":
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(distributeApi.get("name")));
              } else {
                orderList.add(criteriaBuilder.desc(distributeApi.get("name")));
              }
              break;
            case "url":
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(distributeApi.get("url")));
              } else {
                orderList.add(criteriaBuilder.desc(distributeApi.get("url")));
              }
              break;
            case "method":
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(distributeApi.get("method")));
              } else {
                orderList.add(criteriaBuilder.desc(distributeApi.get("method")));
              }
              break;
            case "status":
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(distributeApi.get("status")));
              } else {
                orderList.add(criteriaBuilder.desc(distributeApi.get("status")));
              }
              break;
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
}
