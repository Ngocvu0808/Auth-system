package com.ttt.mar.auth.repositories;

import com.ttt.mar.auth.entities.application.Client;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author bontk
 * @created_date 03/06/2020
 */
public interface ClientRepository extends JpaRepository<Client, Integer>,
    JpaSpecificationExecutor<Client> {

  Optional<Client> findByClientId(String clientId);

  Page<Client> findAll(Specification<Client> specification, Pageable pageable);

  Optional<Client> findByNameAndIsDeletedFalse(String name);

  Client findByIdAndIsDeletedFalse(Integer id);

  @Query("SELECT DISTINCT c.id FROM Client c WHERE c.ownerId=:ownerId")
  List<Integer> findAllIdByOwnerId(@Param("ownerId") Integer ownerId);
}
