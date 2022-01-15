package com.ttt.mar.leads.repositories;

import com.ttt.mar.leads.entities.CampaignFilter;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignFilterRepository extends JpaRepository<CampaignFilter, Integer>,
    JpaSpecificationExecutor<CampaignFilter> {

  List<CampaignFilter> findCampaignFilterByFilterIdAndIsDeletedIsFalse(Integer filterId);

  List<CampaignFilter> findCampaignFilterByCampaignIdAndIsDeletedIsFalse(Integer campaignId);

  CampaignFilter findByCampaignIdAndIdAndIsDeletedFalse(Integer campaignId,
      Integer id);

  List<CampaignFilter> findByCampaignIdAndFilterIdAndIsDeletedFalse(Integer campaignId,
      Integer id);


}
