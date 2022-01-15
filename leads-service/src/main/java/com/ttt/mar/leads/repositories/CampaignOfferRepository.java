package com.ttt.mar.leads.repositories;

import com.ttt.mar.leads.entities.CampaignOffer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignOfferRepository extends JpaRepository<CampaignOffer, Integer>,
    JpaSpecificationExecutor<CampaignOffer> {

  List<CampaignOffer> findCampaignOfferByCampaignIdAndIsDeletedIsFalse(Integer campaignId);

  List<CampaignOffer> findAllByCampaignIdAndIsDeletedFalse(Integer campaignId);

  List<CampaignOffer> findByCampaignIdAndOfferIdAndIsDeletedFalse(Integer campaignId,
      Integer offerId);

  CampaignOffer findByCampaignIdAndIdAndIsDeletedFalse(Integer campaignId, Integer id);

}
