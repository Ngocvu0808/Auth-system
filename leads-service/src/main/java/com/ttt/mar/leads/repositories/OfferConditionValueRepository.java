package com.ttt.mar.leads.repositories;

import com.ttt.mar.leads.entities.OfferConditionValue;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OfferConditionValueRepository extends JpaRepository<OfferConditionValue, Integer> {

  @Query(value = "SELECT ocv.value FROM OfferConditionValue ocv WHERE ocv.isDeleted = false AND ocv.offerConditionId = :offerConditionId ")
  List<String> listConditionValueByOfferConditionId(
      @Param("offerConditionId") Integer offerConditionId);

  List<OfferConditionValue> findByOfferConditionIdInAndIsDeletedFalse(Set<Integer> offerConditions);
}
