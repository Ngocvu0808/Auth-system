package com.ttt.mar.leads.filter;

import com.ttt.mar.leads.entities.Campaign;
import com.ttt.mar.leads.entities.CampaignFilter;
import com.ttt.mar.leads.entities.Filter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class FilterOfCampaignFilter {

  public Specification<CampaignFilter> filter(Integer campaignId, Map<String, String> sort) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      Join<CampaignFilter, Filter> filterJoin = root.join("filter", JoinType.INNER);
      Join<CampaignFilter, Campaign> campaignJoin = root.join("campaign", JoinType.INNER);
      predicates.add(criteriaBuilder.equal(campaignJoin.get("id"), campaignId));
      predicates.add(criteriaBuilder.equal(campaignJoin.get("isDeleted"), false));
      if (sort != null && !sort.isEmpty()) {
        List<Order> orderList = new ArrayList<>();
        Set<String> keySet = sort.keySet();
        for (String key : keySet) {
          String orderType = sort.get(key);
          if (orderType.equals("asc")) {
            orderList.add(criteriaBuilder.asc(root.get(key)));
          } else {
            orderList.add(criteriaBuilder.desc(root.get(key)));
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
