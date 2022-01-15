package com.ttt.mar.leads.filter;

import com.ttt.mar.leads.entities.ProjectSource;
import com.ttt.mar.leads.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
@Component
public class ProjectSourceFilter {

  private static final Logger logger = LoggerFactory.getLogger(ProjectSourceFilter.class);

  public Specification<ProjectSource> filter(Integer projectId, Map<String, String> sort) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      predicates.add(criteriaBuilder.equal(root.get("projectId"), projectId));
      Join<Object, Object> leadSource = root.join("leadSource", JoinType.LEFT);
      predicates.add(criteriaBuilder.equal(leadSource.get("isDeleted"), false));

      if (sort != null && !sort.isEmpty()) {
        List<Order> orderList = new ArrayList<>();
        Set<String> keySet = sort.keySet();
        for (String key : keySet) {
          String orderType = sort.get(key);
          switch (key) {
            case "nameSource":
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(leadSource.get("name")));
              } else {
                orderList.add(criteriaBuilder.desc(leadSource.get("name")));
              }
              break;
            case "utmSource":
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(leadSource.get("utmSource")));
              } else {
                orderList.add(criteriaBuilder.desc(leadSource.get("utmSource")));
              }
              break;
            case "source":
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(leadSource.get("source")));
              } else {
                orderList.add(criteriaBuilder.desc(leadSource.get("source")));
              }
              break;
            case "status":
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(leadSource.get("status")));
              } else {
                orderList.add(criteriaBuilder.desc(leadSource.get("status")));
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
