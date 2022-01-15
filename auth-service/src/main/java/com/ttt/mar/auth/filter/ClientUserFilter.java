package com.ttt.mar.auth.filter;

import com.ttt.mar.auth.entities.application.ClientUser;
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

/**
 * @author bontk
 * @created_date 09/03/2020
 */
public class ClientUserFilter extends EntityFilter<ClientUser> {

  public Specification<ClientUser> filter(Integer clientId, Set<Integer> roles, String search,
      Map<String, String> sort, Boolean isDeleted) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      Join<Object, Object> clientPermissions = root.join("permissions", JoinType.LEFT);
      Join<Object, Object> role = clientPermissions.join("role", JoinType.LEFT);

      Join<Object, Object> user = root.join("user", JoinType.LEFT);
      Join<Object, Object> client = root.join("client", JoinType.LEFT);

      List<Predicate> predicates = new ArrayList<>();
      predicates.add(criteriaBuilder.equal(client.get("id"), clientId));
      predicates.add(criteriaBuilder.equal(root.get("isDeleted"), isDeleted));
      if (search != null && !search.isBlank()) {
        String searchValue = "%" + search.toLowerCase() + "%";
        Predicate pr1 = criteriaBuilder.like(user.get("name"), searchValue);
        Predicate pr2 = criteriaBuilder.like(user.get("username"), searchValue);
        Predicate pr3 = criteriaBuilder.like(user.get("email"), searchValue);
        Predicate pr4 = criteriaBuilder
            .and(criteriaBuilder.equal(clientPermissions.get("isDeleted"), false),
                criteriaBuilder.like(role.get("name"), searchValue));
        predicates.add(criteriaBuilder.or(pr1, pr2, pr3, pr4));
      }
      if (roles != null && !roles.isEmpty()) {
        predicates.add(
            criteriaBuilder.and(criteriaBuilder.equal(clientPermissions.get("isDeleted"), false),
                criteriaBuilder.equal(role.get("isDeleted"), false),
                criteriaBuilder.in(role.get("id")).value(roles)));
      }

      // sort
      if (sort != null && !sort.isEmpty()) {
        List<Order> orderList = new ArrayList<>();
        Set<String> keySet = sort.keySet();
        for (String key : keySet) {
          String orderType = sort.get(key);
          if (orderType.equals("asc")) {
            orderList.add(criteriaBuilder.asc(user.get(key)));
          } else {
            orderList.add(criteriaBuilder.desc(user.get(key)));
          }
        }
        criteriaQuery.orderBy(orderList);
      }
      return statisticPredicate(root, criteriaBuilder, criteriaQuery, predicates.size(),
          predicates);
    };
  }
}