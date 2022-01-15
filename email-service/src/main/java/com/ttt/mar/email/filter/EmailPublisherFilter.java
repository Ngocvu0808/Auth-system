package com.ttt.mar.email.filter;

import com.ttt.mar.email.entities.EmailPublisher;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class EmailPublisherFilter {

  public Specification<EmailPublisher> filter(String search, Map<String, String> sort) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (StringUtils.isNotBlank(search)) {
        String searchValue = "%" + search.toLowerCase() + "%";
        Predicate pr1 = criteriaBuilder.like(root.get("name"), searchValue);
        Predicate pr2 = criteriaBuilder.like(root.get("code"), searchValue);
        Predicate pr3 = criteriaBuilder.like(root.get("endPoint"), searchValue);
        Predicate pr4 = criteriaBuilder.like(root.get("host"), searchValue);
        predicates.add(criteriaBuilder.or(pr1, pr2, pr3, pr4));
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
              break;
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
