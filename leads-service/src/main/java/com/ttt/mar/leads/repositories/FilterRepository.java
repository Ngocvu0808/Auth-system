package com.ttt.mar.leads.repositories;

import com.ttt.mar.leads.entities.Filter;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FilterRepository extends JpaRepository<Filter, Integer>,
    JpaSpecificationExecutor<Filter> {

  Filter findByIdAndIsDeletedIsFalse(Integer id);

  Filter findByCodeAndIsDeletedFalse(String code);

  List<Filter> findByIdNotInAndIsDeletedFalse(Set<Integer> ids);

  List<Filter> findByIsDeletedFalse();
}
