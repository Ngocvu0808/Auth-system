package com.ttt.mar.email.filter;

import com.ttt.mar.email.entities.EmailHistory;
import com.ttt.mar.email.entities.StatusEmail;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;

import com.ttt.rnd.lib.filter.EntityFilter;
import org.springframework.data.jpa.domain.Specification;

public class EmailHistoryFilter extends EntityFilter<EmailHistory> {

  public Specification<EmailHistory> getByFilter(String search, Set<StatusEmail> listStatus,
      List<Integer> listEmailConfig,
      Long fromDate, Long toDate, Map<String, String> sort) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      Join<Object, Object> emailConfig = root.join("emailConfig", JoinType.LEFT);
      List<Predicate> predicates = new ArrayList<>();
      if (search != null && !search.isEmpty()) {
        Predicate pr1 = criteriaBuilder
            .like(root.get("receiver"), "%" + search.toLowerCase() + "%");
        Predicate pr2 = criteriaBuilder
            .like(root.get("content"), "%" + search.toLowerCase() + "%");
        Predicate pr3 = criteriaBuilder
            .like(root.get("attachFileUrl"), "%" + search.toLowerCase() + "%");
        Predicate pr4 = criteriaBuilder.like(root.get("title"), "%" + search.toLowerCase() + "%");

        predicates.add(criteriaBuilder.or(pr1, pr2, pr3, pr4));
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
      if (listEmailConfig != null && !listEmailConfig.isEmpty()) {
        predicates.add(criteriaBuilder.in(emailConfig.get("id")).value(listEmailConfig));
      }
      //sort
      if (sort != null && !sort.isEmpty()) {
        List<Order> orderList = new ArrayList<>();
        Set<String> keySet = sort.keySet();
        for (String key : keySet) {
          String orderType = sort.get(key);
          switch (key) {
//            case "sender":
//              if (orderType.equals("asc")) {
//                orderList.add(criteriaBuilder.asc(smsConfig.get("phoneNumber")));
//              } else {
//                orderList.add(criteriaBuilder.desc(smsConfig.get("phoneNumber")));
//              }
//              break;
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
