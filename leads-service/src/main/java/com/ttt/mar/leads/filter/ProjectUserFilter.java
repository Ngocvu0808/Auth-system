package com.ttt.mar.leads.filter;

import com.ttt.mar.leads.entities.ProjectUser;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * @author kietdt
 * @created_date 22/04/2021
 */
@Component
public class ProjectUserFilter {

  public Specification<ProjectUser> filter(Integer projectId) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (projectId != null) {
        predicates.add(criteriaBuilder.equal(root.get("projectId"), projectId));
      }
      predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
      criteriaQuery.distinct(true);
      return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    };
  }
}
