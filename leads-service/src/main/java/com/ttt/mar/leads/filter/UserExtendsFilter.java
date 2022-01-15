package com.ttt.mar.leads.filter;

import com.ttt.rnd.lib.entities.User;
import com.ttt.rnd.lib.entities.UserStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class UserExtendsFilter {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(LeadSourceFilter.class);

  public Specification<User> filterUserNotExistProject(Set<Integer> ids, UserStatus status) {
    logger.info("--- filterUserNotExistProject() ---");
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (ids != null && !ids.isEmpty()) {
        predicates.add(criteriaBuilder.in(root.get("id")).value(ids).not());
      }
      if (status != null) {
        predicates.add(criteriaBuilder.equal(root.get("status"), status));
      }
      predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
      criteriaQuery.distinct(true);
      return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    };
  }
}
