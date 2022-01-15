package com.ttt.mar.auth.filter;

import com.ttt.mar.auth.entities.application.ClientApi;
import com.ttt.rnd.lib.filter.EntityFilter;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author bontk
 * @created_date 05/06/2020
 */
public class ClientApiFilter extends EntityFilter<ClientApi> {

  public Specification<ClientApi> filterByClientIdAndServiceId(Integer clientId, Integer serviceId) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      Join<Object, Object> api = root.join("api", JoinType.LEFT);
      Join<Object, Object> service = api.join("service", JoinType.LEFT);

      List<Predicate> predicates = new ArrayList<>();
      predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
      predicates.add(criteriaBuilder.equal(root.get("clientId"), clientId));
      predicates.add(criteriaBuilder.equal(service.get("id"), serviceId));
      predicates.add(criteriaBuilder.equal(service.get("isDeleted"), false));
      return statisticPredicate(root, criteriaBuilder, criteriaQuery, predicates.size(),
          predicates);
    };
  }
}