package com.ttt.mar.auth.repositories;

import com.ttt.mar.auth.entities.service.ExternalApi;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author bontk
 * @created_date 04/08/2020
 */
public interface ExternalApiRepository extends JpaRepository<ExternalApi, Long>,
    JpaSpecificationExecutor<ExternalApi> {

  List<ExternalApi> findAllByServiceIdAndIsDeletedFalse(Integer serviceId);

  Optional<ExternalApi> findByCodeAndServiceIdAndIsDeletedFalse(String code, Integer serviceId);

  Page<ExternalApi> findAll(Specification<ExternalApi> specification, Pageable pageable);
}
