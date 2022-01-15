package com.ttt.mar.email.repositories;

import com.ttt.mar.email.entities.EmailConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmailConfigRepository extends JpaRepository<EmailConfig, Integer>,
    JpaSpecificationExecutor<EmailConfig> {

//  EmailConfig findByChannelCodeAndBrandCodeAndIsDeletedFalse(String channelCode, String brandCode);

  EmailConfig findByIdAndIsDeletedFalse(Integer id);

  EmailConfig findByAccountCodeAndIsDeletedFalse(String accountCode);
}
