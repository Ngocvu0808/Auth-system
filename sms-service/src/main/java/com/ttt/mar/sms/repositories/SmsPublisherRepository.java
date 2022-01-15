package com.ttt.mar.sms.repositories;

import com.ttt.mar.sms.entities.SmsPublisher;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SmsPublisherRepository extends JpaRepository<SmsPublisher, Integer>,
    JpaSpecificationExecutor<SmsPublisher> {

  SmsPublisher findByCodeAndIsDeletedFalse(String code);

  SmsPublisher findByIdAndIsDeletedFalse(Integer id);

  List<SmsPublisher> findAllByIsDeletedFalse();
}
