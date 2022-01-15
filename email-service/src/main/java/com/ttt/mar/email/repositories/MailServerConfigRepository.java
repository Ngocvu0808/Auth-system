package com.ttt.mar.email.repositories;

import com.ttt.mar.email.entities.MailServerConfig;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author bontk
 * @created_date 17/07/2020
 */
public interface MailServerConfigRepository extends JpaRepository<MailServerConfig, Integer> {

  Optional<MailServerConfig> findByCode(String code);
}
