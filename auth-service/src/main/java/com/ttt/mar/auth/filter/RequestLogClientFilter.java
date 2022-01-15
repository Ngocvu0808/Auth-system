package com.ttt.mar.auth.filter;


import com.ttt.mar.auth.entities.enums.ClientAuthType;
import com.ttt.rnd.lib.entities.LogRequest;
import com.ttt.rnd.lib.filter.EntityFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class RequestLogClientFilter extends EntityFilter<LogRequest> {

  public Specification<LogRequest> getByFilter(String search, Long fromDate, Long toDate,
      Set<String> listToken, List<String> listApiKey, ClientAuthType authType,
      List<Integer> status, Map<String, String> sort) {

    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (authType != null && authType.equals(ClientAuthType.OAUTH) && listToken != null) {

        predicates.add(criteriaBuilder.in(root.get("token")).value(listToken));
        predicates.add(criteriaBuilder.equal(root.get("authType"), "access-token"));

        if (search != null && !search.isEmpty()) {
          Predicate p1 = criteriaBuilder.like(root.get("apiUrl"), "%" + search.toLowerCase() + "%");
          Predicate p2 = criteriaBuilder.like(root.get("method"), "%" + search.toLowerCase() + "%");
          Predicate p3 = criteriaBuilder.like(root.get("ip"), "%" + search.toLowerCase() + "%");
          predicates.add(criteriaBuilder.or(p1, p2, p3));
        }

        if (status != null && !status.isEmpty()) {
          predicates.add(criteriaBuilder.in(root.get("resHttpCode")).value(status));

        }
        if (fromDate == null) {
          if (toDate == null) {
            predicates.add(criteriaBuilder
                .lessThanOrEqualTo(root.get("createdTime"), new Date(System.currentTimeMillis())));
          } else {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdTime"), toDate));
          }
        } else {
          if (toDate == null) {
            predicates.add(criteriaBuilder
                .between(root.get("createdTime"), new Date(fromDate),
                    new Date(System.currentTimeMillis())));
          } else {
            predicates.add(criteriaBuilder
                .between(root.get("createdTime"), new Date(fromDate), new Date(toDate)));
          }
        }

        if (sort != null && !sort.isEmpty()) {
          List<Order> orderList = new ArrayList<>();
          Set<String> keySet = sort.keySet();
          for (String key : keySet) {
            String orderType = sort.get(key);
            switch (key) {
              case "nameUser":
                break;
              default:
                if (orderType.equals("asc")) {
                  orderList.add(criteriaBuilder.asc(root.get(key)));
                } else {
                  orderList.add(criteriaBuilder.desc(root.get(key)));
                }
            }
            criteriaQuery.orderBy(orderList);
          }
        }
      }

      if (authType != null && authType.equals(ClientAuthType.API_KEY) && listApiKey != null) {

        predicates.add(criteriaBuilder.in(root.get("token")).value(listApiKey));
        predicates.add(criteriaBuilder.equal(root.get("authType"), "api-key"));

        if (search != null && !search.isEmpty()) {
          Predicate p1 = criteriaBuilder.like(root.get("apiUrl"), "%" + search.toLowerCase() + "%");
          Predicate p2 = criteriaBuilder.like(root.get("method"), "%" + search.toLowerCase() + "%");
          Predicate p3 = criteriaBuilder.like(root.get("ip"), "%" + search.toLowerCase() + "%");
          predicates.add(criteriaBuilder.or(p1, p2, p3));
        }

        if (status != null && !status.isEmpty()) {
          predicates.add(criteriaBuilder.in(root.get("resHttpCode")).value(status));

        }
        if (fromDate == null && toDate == null) {
          predicates.add(criteriaBuilder
              .lessThanOrEqualTo(root.get("createdTime"), new Date(System.currentTimeMillis())));
        } else {
          if (fromDate == null && toDate != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdTime"), toDate));
          } else {
            if (fromDate != null && toDate == null) {
              predicates.add(criteriaBuilder
                  .between(root.get("createdTime"), new Date(fromDate),
                      new Date(System.currentTimeMillis())));
            } else {
              predicates.add(criteriaBuilder
                  .between(root.get("createdTime"), new Date(fromDate), new Date(toDate)));
            }
          }
        }

        if (sort != null && !sort.isEmpty()) {
          List<Order> orderList = new ArrayList<>();
          Set<String> keySet = sort.keySet();
          for (String key : keySet) {
            if (key.equals("nameUser")) {
              continue;
            }
            String orderType = sort.get(key);
            if (orderType.equals("asc")) {
              orderList.add(criteriaBuilder.asc(root.get(key)));
            } else {
              orderList.add(criteriaBuilder.desc(root.get(key)));
            }
            criteriaQuery.orderBy(orderList);
          }
        }
      }
      criteriaQuery.distinct(true);
      return statisticPredicate(root, criteriaBuilder, criteriaQuery, predicates.size(),
          predicates);
    };
  }
}
