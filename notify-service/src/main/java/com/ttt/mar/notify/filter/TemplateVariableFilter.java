package com.ttt.mar.notify.filter;

import com.ttt.mar.notify.entities.template.TemplateVariables;
import com.ttt.mar.notify.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TemplateVariableFilter {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(TemplateVariableFilter.class);

  public Specification<TemplateVariables> filter(String search, Map<String, String> sort) {
    logger.info("--- TemplateVariableFilter(), search: {} ---", search);
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (search != null && !search.isEmpty()) {
        logger.info("value of search: " + search);
        String searchConvert = Utils.textSpecialCharacters(search);
        logger.info("value of searchConvert: " + searchConvert);
        String searchValue = "%" + searchConvert.toLowerCase() + "%";
        logger.info("value of search value: " + searchValue);
        Predicate pr1 = criteriaBuilder.like(root.get("name"), searchValue);
        Predicate pr2 = criteriaBuilder.like(root.get("code"), searchValue);

        predicates.add(criteriaBuilder.or(pr1, pr2));
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
