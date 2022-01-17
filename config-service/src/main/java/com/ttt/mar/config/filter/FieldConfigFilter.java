package com.ttt.mar.config.filter;

import com.ttt.mar.config.entities.MarFieldConfig;
import com.ttt.mar.config.entities.MarFieldConfigType;
import com.ttt.rnd.lib.filter.EntityFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class FieldConfigFilter extends EntityFilter<MarFieldConfig> {

  public Specification<MarFieldConfig> getByFilter(List<String> types,
      String search, Map<String, String> sort, boolean isdeleted) {
    return (root, criteriaQuery, criteriaBuilder) -> {

      List<Predicate> predicates = new ArrayList<>();

      if (search != null && !search.isEmpty()) {
        String searchLike = "%" + search.toLowerCase() + "%";
        Predicate p1 = criteriaBuilder.like(root.get("name"), searchLike);
        Predicate p2 = criteriaBuilder.like(root.get("key"), searchLike);
        Predicate p3 = criteriaBuilder.like(root.get("formatValue"), searchLike);
        Predicate p4 = criteriaBuilder.like(root.get("id").as(String.class), searchLike);
        Predicate p5 = criteriaBuilder.like(root.get("type").as(String.class), searchLike);
        Predicate p6 = criteriaBuilder.like(root.get("typeValue").as(String.class), searchLike);
        Predicate p7 = criteriaBuilder.like(root.get("object").as(String.class), searchLike);
        Predicate pre = criteriaBuilder.or(p1, p2, p3, p4, p5, p6, p7);
        predicates.add(pre);
      }
      if (sort != null && !sort.isEmpty()) {
        List<Order> orderList = new ArrayList<>();
        Set<String> keySet = sort.keySet();
        for (String key : keySet) {
          String orderType = sort.get(key);
          if ("creatorName".equals(key)) {
            Join<Object, Object> creatorUser = root.join("creatorUser");
            if (orderType.equals("asc")) {
              orderList.add(criteriaBuilder.asc(creatorUser.get("name")));
            } else {
              orderList.add(criteriaBuilder.desc(creatorUser.get("name")));
            }
          } else {
            if (orderType.equals("asc")) {
              orderList.add(criteriaBuilder.asc(root.get(key)));
            } else {
              orderList.add(criteriaBuilder.desc(root.get(key)));
            }
          }
        }
        criteriaQuery.orderBy(orderList);
      }

      List<MarFieldConfigType> list = new ArrayList<>();
      types.forEach(type -> {
        try {
          list.add(MarFieldConfigType.valueOf(type));
        } catch (Exception ignored) {

        }
      });
      if (list.size() > 0) {
        predicates.add(criteriaBuilder.in(root.get("type")).value(list));
      }
      predicates.add(criteriaBuilder.equal(root.get("isDeleted"), isdeleted));
      return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    };
  }

  public Specification<MarFieldConfig> getByFilter(String types, String objects) {
    return (root, criteriaQuery, criteriaBuilder) -> {

      List<Predicate> predicates = new ArrayList<>();

      List<MarFieldConfigType> list = new ArrayList<>();
      List<String> typeList = new ArrayList<>(Arrays.asList(types.split(",")));
      typeList.forEach(type -> {
        try {
          list.add(MarFieldConfigType.valueOf(type));
        } catch (Exception ignored) {
        }
      });
      if (list.size() > 0) {
        predicates.add(criteriaBuilder.in(root.get("type")).value(list));
      }
      if (objects != null && !objects.isEmpty()) {
        List<String> objectArr = new ArrayList<>(Arrays.asList(objects.split(",")));
        objectArr.add("GLOBAL");
        predicates.add(criteriaBuilder.in(root.get("object")).value(objectArr));
      }
      predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
      return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    };
  }
}
