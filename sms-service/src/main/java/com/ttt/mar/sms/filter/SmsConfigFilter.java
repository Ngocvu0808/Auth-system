package com.ttt.mar.sms.filter;

import com.ttt.mar.sms.entities.SmsConfig;
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

public class SmsConfigFilter extends EntityFilter<SmsConfig> {

  public Specification<SmsConfig> getByFilter(String search, Integer publisherId,
      Boolean isDeleted, Map<String, String> sort) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      Join<Object, Object> marUser = root.join("creatorUser", JoinType.LEFT);
      Join<Object, Object> smsPublisher = root.join("smsPublisher", JoinType.LEFT);
      List<Predicate> predicates = new ArrayList<>();
      if (search != null && !search.isEmpty()) {
        Predicate pr1 = criteriaBuilder
            .like(root.get("brandName"), "%" + search.toLowerCase() + "%");
        Predicate pr2 = criteriaBuilder
            .like(root.get("accountCode"), "%" + search.toLowerCase() + "%");
        Predicate pr3 = criteriaBuilder
            .like(root.get("phoneNumber"), "%" + search.toLowerCase() + "%");
        Predicate pr4 = criteriaBuilder
            .like(smsPublisher.get("name"), "%" + search.toLowerCase() + "%");
        predicates.add(criteriaBuilder.or(pr1, pr2, pr3, pr4));
      }
      if (publisherId != null) {
        predicates.add(criteriaBuilder.equal(smsPublisher.get("id"), publisherId));
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
                orderList.add(criteriaBuilder.asc(marUser.get("name")));
              } else {
                orderList.add(criteriaBuilder.desc(marUser.get("name")));
              }
              break;
            case "publisherName":
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(smsPublisher.get("name")));
              } else {
                orderList.add(criteriaBuilder.desc(smsPublisher.get("name")));
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

      predicates.add(criteriaBuilder.equal(smsPublisher.get("isDeleted"), false));
      predicates.add(criteriaBuilder.equal(root.get("isDeleted"), isDeleted));
      return statisticPredicate(root, criteriaBuilder, criteriaQuery, predicates.size(),
          predicates);
    };
  }
}
