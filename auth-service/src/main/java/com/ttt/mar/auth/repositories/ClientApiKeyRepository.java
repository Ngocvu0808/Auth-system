package com.ttt.mar.auth.repositories;

import com.ttt.mar.auth.entities.application.ClientApiKey;
import com.ttt.mar.auth.entities.enums.ClientApiKeyStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @author bontk
 * @created_date 17/09/2020
 */
public interface ClientApiKeyRepository extends JpaRepository<ClientApiKey, Integer>,
    JpaSpecificationExecutor<ClientApiKey> {

  List<ClientApiKey> findAllByClientIdAndStatusAndIsDeletedFalse(Integer clientId,
      ClientApiKeyStatus status);

  Optional<ClientApiKey> findByApiKeyAndIsDeletedFalse(String apiKey);

  @Query("SELECT ca FROM ClientApiKey ca WHERE ca.clientId=:clientId AND ca.isDeleted=FALSE AND ca.status='ACTIVE'")
  Optional<ClientApiKey> findApiKeyActiveOfClient(Integer clientId);

  Optional<ClientApiKey> findByApiKey(String apiKey);
}
