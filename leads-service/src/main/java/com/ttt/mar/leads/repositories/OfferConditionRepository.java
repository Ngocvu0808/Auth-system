package com.ttt.mar.leads.repositories;

import com.ttt.mar.leads.entities.OfferCondition;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferConditionRepository extends JpaRepository<OfferCondition, Integer> {

  List<OfferCondition> findOfferConditionByOfferIdAndAndIsDeletedIsFalse(Integer offerId);

  List<OfferCondition> findByOfferIdAndIsDeletedFalse(Integer offerId);
}
