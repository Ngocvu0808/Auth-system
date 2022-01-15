package com.ttt.mar.email.repositories;

import com.ttt.mar.email.entities.EmailHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmailHistoryRepository extends JpaRepository<EmailHistory, Long>,
    JpaSpecificationExecutor<EmailHistory> {

}
