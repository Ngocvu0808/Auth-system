package com.ttt.mar.leads.repositories;

import com.ttt.mar.leads.entities.FilterCondition;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilterConditionRepository extends JpaRepository<FilterCondition, Integer> {

  List<FilterCondition> findFilterConditionByFilterIdAndIsDeletedFalse(Integer filterId);
}
