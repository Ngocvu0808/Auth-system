package com.ttt.mar.config.filter;

import com.ttt.mar.config.entities.MarConfig;
import com.ttt.rnd.lib.filter.EntityFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ConfigFilter extends EntityFilter<MarConfig> {

  public Specification<MarConfig> getByFilter(List<String> keys, String search,
      Map<String, String> sort, boolean isDeleted) {
    return (root, criteriaQuery, criteriaBuilder) -> {

      List<Predicate> predicates = new ArrayList<>();
      if (keys != null && keys.size() > 0) {
        predicates.add(criteriaBuilder.in(root.get("key")).value(keys));
      }
      if (search != null && !search.isEmpty()) {
        String searchValue = "%" + search.toLowerCase() + "%";
        Predicate pr1 = criteriaBuilder.like(root.get("key"), searchValue);
        Predicate pr2 = criteriaBuilder.like(root.get("value"), searchValue);
        Predicate pr3 = criteriaBuilder.like(root.get("id").as(String.class), searchValue);
        predicates.add(criteriaBuilder.or(pr1, pr2, pr3));
      }
      predicates.add(criteriaBuilder.equal(root.get("isDeleted"), isDeleted));
      if (sort != null && !sort.isEmpty()) {
        List<Order> orderList = new ArrayList<>();
        Set<String> keySet = sort.keySet();
        for (String key : keySet) {
          String orderType = sort.get(key);
          if ("creatorName".equals(key)) {
            Join<Object, Object> creatorUser = root.join("creatorUser");
            if (orderType.equals("asc")) {
              orderList.add(criteriaBuilder.asc(creatorUser.get("name")));
            } else {
              orderList.add(criteriaBuilder.desc(creatorUser.get("name")));
            }
          } else {
            if (orderType.equals("asc")) {
              orderList.add(criteriaBuilder.asc(root.get(key)));
            } else {
              orderList.add(criteriaBuilder.desc(root.get(key)));
            }
          }
        }
        criteriaQuery.orderBy(orderList);
      }
      return statisticPredicate(root, criteriaBuilder, criteriaQuery, predicates.size(),
          predicates);
    };
  }
}
