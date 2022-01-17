package com.ttt.mar.notify.repositories;

import com.ttt.mar.notify.entities.NotifyHistoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NotifyHistoryStatusRepository extends JpaRepository<NotifyHistoryStatus, Integer>,
    JpaSpecificationExecutor<NotifyHistoryStatus> {

}
