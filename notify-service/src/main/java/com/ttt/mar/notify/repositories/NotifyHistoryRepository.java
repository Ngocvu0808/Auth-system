package com.ttt.mar.notify.repositories;

import com.ttt.mar.notify.entities.NotifyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NotifyHistoryRepository extends JpaRepository<NotifyHistory, Long>,
    JpaSpecificationExecutor<NotifyHistory> {

  NotifyHistory findByIdAndIsDeletedFalse(Long requestId);
}
