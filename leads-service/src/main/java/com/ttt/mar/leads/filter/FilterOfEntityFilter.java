package com.ttt.mar.leads.filter;

import com.ttt.mar.leads.entities.Filter;
import com.ttt.mar.leads.utils.Utils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class FilterOfEntityFilter {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(FilterOfEntityFilter.class);

  public Specification<Filter> filter(String search, Map<String, String> sort, Long startDate,
      Long endDate) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (StringUtils.isNotBlank(search)) {
        String searchConvert = Utils.textSpecialCharacters(search);
        String searchValue = "%" + searchConvert.toLowerCase() + "%";
        Predicate pr1 = criteriaBuilder.like(root.get("name"), searchValue);
        Predicate pr2 = criteriaBuilder.like(root.get("code"), searchValue);
        predicates.add(criteriaBuilder.or(pr1, pr2));
      }
      if (startDate != null) {
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdTime"),
            new Date(startDate)));
      }
      if (endDate != null) {
        predicates.add(
            criteriaBuilder.lessThanOrEqualTo(root.get("createdTime"), new Date(endDate)));
      }
      if (sort != null && !sort.isEmpty()) {
        List<Order> orderList = new ArrayList<>();
        Set<String> keySet = sort.keySet();
        for (String key : keySet) {
          String orderType = sort.get(key);
          switch (key) {
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
      predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
      criteriaQuery.distinct(true);
      return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    };
  }

}
