package com.ttt.mar.notify.repositories;

import com.ttt.mar.notify.entities.StatusNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StatusNotifiCationRepository extends JpaRepository<StatusNotification, Integer>,
    JpaSpecificationExecutor<StatusNotification> {

  StatusNotification findByCode(String code);
}
