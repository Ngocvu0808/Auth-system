package com.ttt.mar.config.filter;

import com.ttt.mar.config.entities.MarFilterConfig;
import com.ttt.rnd.lib.filter.EntityFilter;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class FilterConfigFilter extends EntityFilter<MarFilterConfig> {

  public Specification<MarFilterConfig> getByFilter(String service, String type,
      boolean isDeleted) {
    return (root, criteriaQuery, criteriaBuilder) -> {

      List<Predicate> predicates = new ArrayList<>();
      predicates.add(criteriaBuilder.equal(root.get("filterType"), type));
      predicates.add(criteriaBuilder.equal(root.get("isDeleted"), isDeleted));
      Join<Object, Object> marService = root.join("service");
      predicates.add(criteriaBuilder.equal(marService.get("service"), service));
      return statisticPredicate(root, criteriaBuilder, criteriaQuery, predicates.size(),
          predicates);
    };
  }
}
