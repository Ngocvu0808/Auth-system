package com.ttt.mar.sms.repositories;

import com.ttt.mar.sms.entities.SmsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SmsHistoryRepository extends JpaRepository<SmsHistory, Long>,
    JpaSpecificationExecutor<SmsHistory> {

}
