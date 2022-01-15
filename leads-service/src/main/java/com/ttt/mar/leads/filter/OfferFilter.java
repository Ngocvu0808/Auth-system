package com.ttt.mar.leads.filter;

import com.ttt.mar.leads.entities.CampaignOffer;
import com.ttt.mar.leads.entities.Offer;
import com.ttt.mar.leads.utils.Utils;
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
public class OfferFilter {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(LeadSourceFilter.class);

  public Specification<Offer> filter(String search, Map<String, String> sort, Long startDate,
      Long endDate) {
    logger.info("--- OfferFilter() ---");
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (StringUtils.isNotBlank(search)) {
        logger.info("value of search: " + search);
        String searchConvert = Utils.escapeMetaCharacters(search);
        if (searchConvert.contains("_")) {
          searchConvert = searchConvert.replaceAll("_", "\\\\_");
        }
        String searchValue = "%" + searchConvert.toLowerCase() + "%";
        logger.info(searchValue);
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
