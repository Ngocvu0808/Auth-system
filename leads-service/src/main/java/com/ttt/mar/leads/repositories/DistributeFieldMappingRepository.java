package com.ttt.mar.leads.repositories;

import com.ttt.mar.leads.entities.DistributeFieldMapping;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistributeFieldMappingRepository extends
    JpaRepository<DistributeFieldMapping, Integer> {

  List<DistributeFieldMapping> findByDistributeIdAndIsDeletedFalse(Integer distributeId);

  DistributeFieldMapping findByIdAndDistributeIdAndIsDeletedFalse(Integer id, Integer distributeId);

  List<DistributeFieldMapping> findByIdNotIn(Set<Integer> ids);
}
