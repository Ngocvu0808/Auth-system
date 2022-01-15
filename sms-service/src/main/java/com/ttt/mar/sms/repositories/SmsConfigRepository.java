package com.ttt.mar.sms.repositories;

import com.ttt.mar.sms.entities.SmsConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SmsConfigRepository extends JpaRepository<SmsConfig, Integer>,
    JpaSpecificationExecutor<SmsConfig> {

  SmsConfig findByAccountCodeAndIsDeletedFalse(String accountCode);

  SmsConfig findByIdAndIsDeletedFalse(Integer id);
}
