package com.ttt.mar.notify.repositories;

import com.ttt.mar.notify.entities.TypeSend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TypeSendRepository extends JpaRepository<TypeSend, Integer>,
    JpaSpecificationExecutor<TypeSend> {

  TypeSend findByCode(String code);
}
