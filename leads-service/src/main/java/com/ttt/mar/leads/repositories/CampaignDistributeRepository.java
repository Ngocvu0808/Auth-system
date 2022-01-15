package com.ttt.mar.leads.repositories;

import com.ttt.mar.leads.entities.CampaignDistribute;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CampaignDistributeRepository extends JpaRepository<CampaignDistribute, Integer> {

  List<CampaignDistribute> findByCampaignIdAndIsDeletedFalse(Integer campaignId);

  @Query(value = " Select cd.distributeId  from CampaignDistribute cd Where cd.campaignId in (:listIdCamapign) and cd.isDeleted = false")
  List<Integer> findByListCampaignIdAndIsDeletedFalse(Set<Integer> listIdCamapign);
}
