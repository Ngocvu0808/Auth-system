package com.ttt.mar.leads.repositories;

import com.ttt.mar.leads.entities.FilterConditionValue;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FilterConditionValueRepository extends
    JpaRepository<FilterConditionValue, Integer> {

  @Query(value = "SELECT  fcv.value FROM FilterConditionValue  fcv WHERE fcv.isDeleted =false  AND fcv.filterConditionId= :filterConditionId")
  List<String> listFilterValueByConditionId(
      @Param("filterConditionId") Integer filterConditionId);

  List<FilterConditionValue> findByFilterConditionIdInAndIsDeletedFalse(
      Set<Integer> filterConditionIds);
}
