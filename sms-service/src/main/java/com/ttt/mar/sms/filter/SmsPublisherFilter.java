package com.ttt.mar.sms.filter;

import com.ttt.mar.sms.entities.SmsPublisher;
import com.ttt.rnd.lib.filter.EntityFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class SmsPublisherFilter extends EntityFilter<SmsPublisher> {

  public Specification<SmsPublisher> getByFilter(String search, Boolean isDeleted,
      Map<String, String> sort) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      Join<Object, Object> creatorUser = root.join("creatorUser", JoinType.LEFT);
      List<Predicate> predicates = new ArrayList<>();
      if (search != null && !search.isEmpty()) {
        Predicate pr1 = criteriaBuilder
            .like(root.get("code"), "%" + search.toLowerCase() + "%");
        Predicate pr2 = criteriaBuilder
            .like(root.get("name"), "%" + search.toLowerCase() + "%");
        Predicate pr3 = criteriaBuilder
            .like(root.get("endPoint"), "%" + search.toLowerCase() + "%");
        predicates.add(criteriaBuilder.or(pr1, pr2, pr3));
      }

      //sort
      if (sort != null && !sort.isEmpty()) {
        List<Order> orderList = new ArrayList<>();
        Set<String> keySet = sort.keySet();
        for (String key : keySet) {
          String orderType = sort.get(key);
          switch (key) {
            case "creatorName":
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(creatorUser.get("username")));
              } else {
                orderList.add(criteriaBuilder.desc(creatorUser.get("userName")));
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
      predicates.add(criteriaBuilder.equal(root.get("isDeleted"), isDeleted));

      return statisticPredicate(root, criteriaBuilder, criteriaQuery, predicates.size(),
          predicates);
    };
  }
}
