package com.ttt.mar.email.repositories;

import com.ttt.mar.email.entities.EmailPublisher;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmailPublisherRepository extends JpaRepository<EmailPublisher, Integer>,
    JpaSpecificationExecutor<EmailPublisher> {

  Optional<EmailPublisher> findByCodeAndIsDeletedFalse(String code);

  EmailPublisher findByIdAndIsDeletedFalse(Integer id);

  List<EmailPublisher> findAllByIsDeletedFalse();
}
