package com.ttt.mar.leads.filter;

import com.ttt.mar.leads.entities.Campaign;
import com.ttt.mar.leads.entities.CampaignStatus;
import com.ttt.mar.leads.entities.ProjectStatus;
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

/**
 * @author NamBN
 * @created_date 10/06/2021
 */
@Component
public class CampaignsFilter {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(LeadSourceFilter.class);

  public Specification<Campaign> filter(
      Integer projectId, Integer leadSourceId, Integer distributeId, ProjectStatus projectStatus,
      String search,
      Map<String, String> sort) {
    logger.info("--- CampaignFilter() ---");
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      Join<Object, Object> campaignSources = null;
      Join<Object, Object> leadSource = null;
      Join<Object, Object> campaignDistributes = null;
      Join<Object, Object> distributeApi = null;
      Join<Object, Object> project = root.join("project", JoinType.LEFT);
      predicates.add(criteriaBuilder.equal(project.get("isDeleted"), false));

      if (StringUtils.isNotBlank(search)) {
        logger.info("value of search: " + search);
        String searchConvert = Utils.textSpecialCharacters(search);
        logger.info("value of searchConvert: " + searchConvert);
        String searchValue = "%" + searchConvert.toLowerCase() + "%";
        Predicate pr1 = criteriaBuilder.like(root.get("name"), searchValue);
        Predicate pr2 = criteriaBuilder.like(root.get("code"), searchValue);
        predicates.add(criteriaBuilder.or(pr1, pr2));
      }
      if (projectId != null) {
        predicates.add(criteriaBuilder.equal(project.get("id"), projectId));
      }
      if (projectStatus != null) {
        predicates.add(criteriaBuilder.equal(project.get("status"), projectStatus));
      }
      if (leadSourceId != null) {
        campaignSources = root.join("campaignSources", JoinType.LEFT);
        predicates.add(criteriaBuilder.equal(campaignSources.get("isDeleted"), false));
        leadSource = campaignSources.join("leadSource", JoinType.LEFT);
        predicates.add(criteriaBuilder.equal(leadSource.get("id"), leadSourceId));
        predicates.add(criteriaBuilder.equal(leadSource.get("isDeleted"), false));
      }
      if (distributeId != null) {
        campaignDistributes = root.join("campaignDistributes", JoinType.LEFT);
        predicates.add(criteriaBuilder.equal(campaignDistributes.get("isDeleted"), false));
        distributeApi = campaignDistributes
            .join("distributeApi", JoinType.LEFT);
        predicates.add(criteriaBuilder.equal(distributeApi.get("id"), distributeId));
        predicates.add(criteriaBuilder.equal(distributeApi.get("isDeleted"), false));
      }
      if (sort != null && !sort.isEmpty()) {
        List<Order> orderList = new ArrayList<>();
        Set<String> keySet = sort.keySet();
        for (String key : keySet) {
          String orderType = sort.get(key);
          switch (key) {
            case "leadSourceName":
              if (campaignSources == null) {
                campaignSources = root.join("campaignSources", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(campaignSources.get("isDeleted"), false));
                leadSource = campaignSources.join("leadSource", JoinType.LEFT);
              }
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(leadSource.get("name")));
              } else {
                orderList.add(criteriaBuilder.desc(leadSource.get("name")));
              }
              break;
            case "distributeName":
              if (campaignDistributes == null) {
                campaignDistributes = root
                    .join("campaignDistributes", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(campaignDistributes.get("isDeleted"), false));
                distributeApi = campaignDistributes
                    .join("distributeApi", JoinType.LEFT);
              }
              if (orderType.equals("asc")) {
                orderList.add(criteriaBuilder.asc(distributeApi.get("name")));
              } else {
                orderList.add(criteriaBuilder.desc(distributeApi.get("name")));
              }
              break;
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

  public Specification<Campaign> filterExpired(CampaignStatus status, Long expiredTime) {
    logger.info("--- filterExpired() ---");
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      Join<Object, Object> project = root.join("project", JoinType.LEFT);
      if (status != null) {
        predicates.add(criteriaBuilder.equal(root.get("status"), status));
      }
      if (expiredTime != null) {
        predicates.add(criteriaBuilder.lessThan(root.get("endDate"), new Date(expiredTime)));
      }
      predicates.add(criteriaBuilder.equal(project.get("isDeleted"), false));
      predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
      criteriaQuery.distinct(true);
      return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    };
  }
  public Specification<Campaign> filterCode(String search){
    return (root,criteriaQuery, criteriaBuilder) ->{
      List<Predicate> predicates = new ArrayList<>();
      if (StringUtils.isNotBlank(search)) {
        logger.info("value of search: " + search);
        String searchConvert = Utils.textSpecialCharacters(search);
        logger.info("value of searchConvert: " + searchConvert);
        String searchValue = "%" + searchConvert.toLowerCase() + "%";
        Predicate pr1 = criteriaBuilder.like(root.get("name"), searchValue);
        Predicate pr2 = criteriaBuilder.like(root.get("code"), searchValue);
        predicates.add(criteriaBuilder.or(pr1, pr2));
      }
      predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
      criteriaQuery.distinct(true);
      return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    };
  }
}
