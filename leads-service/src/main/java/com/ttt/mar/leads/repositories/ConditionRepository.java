package com.ttt.mar.leads.repositories;

import com.ttt.mar.leads.entities.Condition;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConditionRepository extends JpaRepository<Condition, Integer> {

  Set<Condition> findByIdInAndIsDeletedFalse(Set<Integer> ids);

}
