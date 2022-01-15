package com.ttt.mar.auth.repositories;

import com.ttt.mar.auth.entities.application.ApiRequest;
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
public interface ApiRequestRepository extends JpaRepository<ApiRequest, Long>,
    JpaSpecificationExecutor<ApiRequest> {

  Page<ApiRequest> findAll(Specification<ApiRequest> specification, Pageable pageable);

  Optional<ApiRequest> findByClientIdAndApiIdAndIsDeletedFalse(Integer clientId, Long apiId);

}
