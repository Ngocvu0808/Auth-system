package com.ttt.mar.email.filter;

import com.ttt.mar.email.entities.EmailConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;

import com.ttt.rnd.lib.filter.EntityFilter;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class EmailConfigFilter extends EntityFilter<EmailConfig> {

  public Specification<EmailConfig> getByFilter(Integer publisherId, String search,
      Map<String, String> sort) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      Join<Object, Object> rootPublisher = root.join("emailPublisher", JoinType.LEFT);
      List<Predicate> predicates = new ArrayList<>();
      if (StringUtils.isNotBlank(search)) {
        String valueSearch = "%" + search.toLowerCase() + "%";
        Predicate pr1 = criteriaBuilder.like(root.get("accountCode"), valueSearch);
        Predicate pr2 = criteriaBuilder.like(root.get("brandName"), valueSearch);
        Predicate pr3 = criteriaBuilder.like(root.get("sender"), valueSearch);
        predicates.add(criteriaBuilder.or(pr1, pr2, pr3));
      }
      if (publisherId != null) {
        predicates.add(criteriaBuilder.in(root.get("publisherId")).value(publisherId));
      }

      //sort
      if (sort != null && !sort.isEmpty()) {
        List<Order> orderList = new ArrayList<>();
        Set<String> keySet = sort.keySet();
        for (String key : keySet) {
          String orderType = sort.get(key);
          switch (key) {
            case "creatorName":
              Join<Object, Object> rootUser = root.join("creatorUser", JoinType.LEFT);
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(rootUser.get("username")));
              } else {
                orderList.add(criteriaBuilder.desc(rootUser.get("username")));
              }
              break;
            case "publisherName":
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(rootPublisher.get("name")));
              } else {
                orderList.add(criteriaBuilder.desc(rootPublisher.get("name")));
              }
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

      predicates.add(criteriaBuilder.equal(rootPublisher.get("isDeleted"), false));
      predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
      return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    };
  }
}
