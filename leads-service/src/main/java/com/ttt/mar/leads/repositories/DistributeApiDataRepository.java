package com.ttt.mar.leads.repositories;

import com.ttt.mar.leads.entities.DistributeApiData;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistributeApiDataRepository extends JpaRepository<DistributeApiData, Integer> {

  List<DistributeApiData> findByDistributeIdAndIsDeletedFalse(Integer distributeId);

  DistributeApiData findByIdAndDistributeIdAndIsDeletedFalse(Integer id, Integer distributeId);

  List<DistributeApiData> findByIdNotIn(Set<Integer> ids);
}
