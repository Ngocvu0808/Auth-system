package com.ttt.mar.leads.repositories;

import com.ttt.mar.leads.entities.LeadSource;
import com.ttt.mar.leads.entities.LeadSourceStatus;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LeadSourceRepository extends JpaRepository<LeadSource, Integer>,
    JpaSpecificationExecutor<LeadSource> {

  LeadSource findByIdAndIsDeletedFalse(Integer id);

  Optional<LeadSource> findByNameAndIsDeletedFalse(String name);

  Optional<LeadSource> findByUtmSourceAndIsDeletedFalse(String utmSource);

  List<LeadSource> findByIdInAndStatusAndIsDeletedFalse(Set<Integer> ids, LeadSourceStatus status);

  List<LeadSource> findAllByStatusAndIsDeletedFalse(LeadSourceStatus status);

  List<LeadSource> findAllByIsDeletedFalse();

  List<LeadSource> findByIdInAndIsDeletedFalse(Set<Integer> ids);
}
