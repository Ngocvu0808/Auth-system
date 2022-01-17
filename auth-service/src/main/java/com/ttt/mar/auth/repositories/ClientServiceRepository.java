package com.ttt.mar.auth.repositories;

import com.ttt.mar.auth.entities.application.ClientService;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ClientServiceRepository extends JpaRepository<ClientService, Integer> {

  Optional<ClientService> findByClientIdAndServiceIdAndIsDeletedFalse(Integer clientId,
      Integer serviceId);

  List<ClientService> findAllByClientIdAndIsDeletedFalse(@Param("clientId") Integer clientId);

}
