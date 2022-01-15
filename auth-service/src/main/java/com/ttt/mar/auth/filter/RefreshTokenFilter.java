package com.ttt.mar.auth.filter;

import com.ttt.mar.auth.entities.application.RefreshToken;
import com.ttt.mar.auth.entities.enums.RefreshTokenStatus;
import com.ttt.rnd.lib.filter.EntityFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author bontk
 * @created_date 18/03/2020
 */
public class RefreshTokenFilter extends EntityFilter<RefreshToken> {

  public Specification<RefreshToken> getByFilter(Integer apiKey, List<String> status,
      Map<String, String> sort, boolean isDeleted) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      Join<Object, Object> client = root.join("client");
      List<Predicate> predicates = new ArrayList<>();
      List<RefreshTokenStatus> list = new ArrayList<>();
      status.forEach(s -> {
        try {
          list.add(RefreshTokenStatus.valueOf(s));
        } catch (Exception ignored) {

        }
      });
      if (list.size() > 0) {
        predicates.add(criteriaBuilder.in(root.get("status")).value(list));
      }

      if (sort != null && !sort.isEmpty()) {
        List<Order> orderList = new ArrayList<>();
        Set<String> keySet = sort.keySet();
        for (String key : keySet) {
          String orderType = sort.get(key);
          switch (key) {
            case "clientName":
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(client.get("name")));
              } else {
                orderList.add(criteriaBuilder.desc(client.get("name")));
              }
              break;
            case "clientId":
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(client.get("clientId")));
              } else {
                orderList.add(criteriaBuilder.desc(client.get("clientId")));
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