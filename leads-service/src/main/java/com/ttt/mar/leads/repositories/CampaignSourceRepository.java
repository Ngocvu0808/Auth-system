package com.ttt.mar.leads.repositories;

import com.ttt.mar.leads.entities.CampaignSource;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author KietDT
 * @created_date 09/06/2021
 */
public interface CampaignSourceRepository extends JpaRepository<CampaignSource, Integer> {

  List<CampaignSource> findByCampaignIdAndIsDeletedFalse(Integer campaignId);

  @Query(value = " Select cs.campaignId  from CampaignSource cs Where cs.campaignId in (:listIdCamapign) and cs.isDeleted = false")
  List<Integer> findByListCampaignIdAndIsDeletedFalse(Set<Integer> listIdCamapign);
}
