package com.ttt.mar.leads.repositories;

import com.ttt.mar.leads.entities.DistributeApiHeader;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistributeApiHeaderRepository extends JpaRepository<DistributeApiHeader, Integer> {

  List<DistributeApiHeader> findByDistributeIdAndIsDeletedFalse(Integer distributeId);

  DistributeApiHeader findByIdAndDistributeIdAndIsDeletedFalse(Integer id, Integer distributeId);

  List<DistributeApiHeader> findByIdNotIn(Set<Integer> ids);
}
