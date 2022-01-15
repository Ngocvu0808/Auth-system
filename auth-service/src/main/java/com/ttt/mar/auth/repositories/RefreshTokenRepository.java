package com.ttt.mar.auth.repositories;

import com.ttt.mar.auth.entities.application.RefreshToken;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author bontk
 * @created_date 03/06/2020
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  Optional<RefreshToken> findByToken(String token);

  List<RefreshToken> findAllByClientIdAndIsDeletedFalse(Integer clientId);

  List<RefreshToken> findAllByClientId(Integer clientId);

  Optional<RefreshToken> findByClientIdAndApiKeyIdAndIpAndIsDeletedFalse(Integer clientId,
      Integer apiKey, String ip);

  Page<RefreshToken> findAll(Specification<RefreshToken> specification, Pageable pageable);
}
