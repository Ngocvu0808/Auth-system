package com.ttt.mar.leads.filter;

import com.ttt.mar.leads.entities.Campaign;
import com.ttt.mar.leads.entities.Schedule;
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
 * @author nguyen
 * @create_date 08/09/2021
 */
public class ScheduleFilter {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(LeadSourceFilter.class);

  public Specification<Schedule> filter(Integer campaignId, Map<String, String> sort) {
    logger.info("--- ScheduleFilter() ---");
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      Join<Schedule, Campaign> campaignJoin = root.join("campaign", JoinType.INNER);
      predicates.add(criteriaBuilder.equal(campaignJoin.get("id"), campaignId));
      predicates.add(criteriaBuilder.equal(campaignJoin.get("isDeleted"), false));
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
