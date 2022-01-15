package com.ttt.mar.auth.repositories;

import com.ttt.mar.auth.entities.service.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author bontk
 * @created_date 03/08/2020
 */
public interface ServiceRepository extends JpaRepository<Service, Integer>,
    JpaSpecificationExecutor<Service> {

  Service findByCodeAndIsDeletedFalse(String code);

  Service findByIdAndIsDeletedFalse(Integer id);

  List<Service> findAllByIsDeletedFalse();

  List<Service> findAllByIdNotInAndIsDeletedFalse(List<Integer> ids);

  Optional<Service> findByCodeAndSystemIdAndIsDeletedFalse(String code, Integer systemId);
}
