package com.ttt.mar.sms.filter;

import com.ttt.mar.sms.entities.SmsHistory;
import com.ttt.rnd.lib.filter.EntityFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class SmsHistoryFilter extends EntityFilter<SmsHistory> {

  public Specification<SmsHistory> getByFilter(String search, Set<String> listStatus,
      Long fromDate, Long toDate, Map<String, String> sort) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      Join<Object, Object> smsConfig = root.join("smsConfig", JoinType.LEFT);
      List<Predicate> predicates = new ArrayList<>();
      if (search != null && !search.isEmpty()) {
        Predicate pr1 = criteriaBuilder
            .like(root.get("receiver"), "%" + search.toLowerCase() + "%");
        Predicate pr2 = criteriaBuilder
            .like(root.get("content"), "%" + search.toLowerCase() + "%");
        Predicate pr3 = criteriaBuilder
            .like(smsConfig.get("phoneNumber"), "%" + search.toLowerCase() + "%");
        predicates.add(criteriaBuilder.or(pr1, pr2, pr3));
      }

      if (listStatus != null && !listStatus.isEmpty()) {
        predicates.add(criteriaBuilder.in(root.get("status")).value(listStatus));
      }
      if (fromDate != null && toDate != null) {
        predicates.add(criteriaBuilder
            .between(root.get("creationTime"), new Date(fromDate), new Date(toDate)));
      }
      if (fromDate != null && toDate == null) {
        predicates.add(criteriaBuilder.between(root.get("creationTime"), new Date(fromDate),
            new Date(System.currentTimeMillis())));
      }
      if (fromDate == null && toDate != null) {
        predicates
            .add(criteriaBuilder.lessThanOrEqualTo(root.get("creationTime"), new Date(toDate)));
      }
      //sort
      if (sort != null && !sort.isEmpty()) {
        List<Order> orderList = new ArrayList<>();
        Set<String> keySet = sort.keySet();
        for (String key : keySet) {
          String orderType = sort.get(key);
          switch (key) {
            case "sender":
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(smsConfig.get("phoneNumber")));
              } else {
                orderList.add(criteriaBuilder.desc(smsConfig.get("phoneNumber")));
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

      return statisticPredicate(root, criteriaBuilder, criteriaQuery, predicates.size(),
          predicates);
    };
  }

}
