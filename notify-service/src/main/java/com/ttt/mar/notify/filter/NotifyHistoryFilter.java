package com.ttt.mar.notify.filter;

import com.ttt.mar.notify.controller.NotifyHistoryController;
import com.ttt.mar.notify.entities.NotifyHistory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class NotifyHistoryFilter {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(NotifyHistoryFilter.class);

  public Specification<NotifyHistory> getByFilter(Long fromDate, Long toDate, Integer typeSendId,
      String search, Map<String, String> sort) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (StringUtils.isNotBlank(search)) {
        String valueSearch = "%" + search.toLowerCase() + "%";
        Predicate pr1 = criteriaBuilder.like(root.get("accountCode"), valueSearch);
        Predicate pr2 = criteriaBuilder.like(root.get("brandName"), valueSearch);
        Predicate pr3 = criteriaBuilder.like(root.get("publisher"), valueSearch);
        Predicate pr4 = criteriaBuilder.like(root.get("receiver"), valueSearch);
        predicates.add(criteriaBuilder.or(pr1, pr2, pr3, pr4));
      }
      if (fromDate == null && toDate != null) {
        logger.info("To Date : {}", new Date(toDate));
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdTime"), new Date(toDate)));
      }
      if (fromDate != null && toDate == null) {
        logger.info("From Date : {}", new Date(fromDate));
        predicates.add(criteriaBuilder.between(root.get("createdTime"), new Date(fromDate), new Date()));
      }
      if (fromDate != null && toDate != null) {
        logger.info("From Date : {}, To Date {}", new Date(fromDate), new Date(toDate));
        predicates.add(criteriaBuilder.between(root.get("createdTime"), new Date(fromDate), new Date(toDate)));
      }
      if (typeSendId != null) {
        Join<Object, Object> rootTypeSend = root.join("typeSend", JoinType.LEFT);
        predicates.add(criteriaBuilder.in(rootTypeSend.get("id")).value(typeSendId));
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
              break;
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