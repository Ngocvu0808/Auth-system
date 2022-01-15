package com.ttt.mar.leads.repositories;

import com.ttt.mar.leads.entities.OfferInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferInfoRepository extends JpaRepository<OfferInfo, Integer>,
    JpaSpecificationExecutor<OfferInfo> {

  List<OfferInfo> findOfferInfoByOfferIdAndIsDeletedIsFalse(Integer offerId);

  List<OfferInfo> findByOfferIdAndIsDeletedFalse(Integer offerId);
}
