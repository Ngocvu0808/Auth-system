package com.ttt.mar.leads.repositories;

import com.ttt.mar.leads.entities.Campaign;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CampaignRepository extends JpaRepository<Campaign, Integer>,
    JpaSpecificationExecutor<Campaign> {

  List<Campaign> findByCodeAndIsDeletedFalse(String code);

  Campaign findByIdAndIsDeletedFalse(Integer id);

  List<Campaign> findAllByIsDeletedFalse();

  List<Campaign> findByProjectIdAndIsDeletedFalse(Integer projectId);
}
